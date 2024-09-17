import {
  BadRequestException,
  ForbiddenException,
  HttpException,
  Injectable,
  NotFoundException,
  UnauthorizedException,
} from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { PrismaService } from '../../../global/prisma/prisma.service';
import { S3Service } from '../../../global/s3/s3.service';
import { CreatePostRequestDto, UpdatePostRequestDto } from '../../../global/dto';
import * as path from 'path';
import { PlaceService } from '../../place/service/place.service';

@Injectable()
export class PostService {
  constructor(
    private readonly configService: ConfigService,
    private readonly prismaService: PrismaService,
    private readonly s3Service: S3Service,
    private readonly placeService: PlaceService,
  ) {}

  async createPost(userId: number, files: Express.Multer.File[], dto: CreatePostRequestDto) {
    const S3URL = this.configService.get<string>('ENV_AWS_S3_URL');
    const { content, menuTag, keywordTag, starRating, placeId } = dto;

    const isPlaceData = await this.prismaService.places.findFirst({ where: { id: placeId } });
    if (!isPlaceData) throw new NotFoundException('잘못된 장소입니다.');

    const menuData = await this.placeService.placeAddMenus(menuTag, placeId);
    const menuTagId = menuData.map((menu) => menu.id.toString()).join(',');

    const uploadToimage = files.map(async (file) => {
      const key = `places/${isPlaceData.id.toString()}/${Date.now()}-${Math.random().toString(16).slice(2)}${path.extname(file.originalname)}`;
      await this.s3Service.uploadS3(key, file.buffer, file.mimetype);
      return S3URL + '/' + key;
    });
    const s3Upload = await Promise.all(uploadToimage);

    const createRate = await this.prismaService.starRatings.create({ data: { star: starRating, placeId } });
    return await this.prismaService.posts.create({
      data: {
        content,
        thumbnailUrl: s3Upload[0],
        imageUrl: s3Upload.length > 1 ? s3Upload.slice(1).toString() : null,
        menuTag: menuTagId,
        keywordTag,
        userId,
        placeId,
        ratingId: createRate.id,
      },
    });
  }

  async findOnePost(id: number) {
    const postData = await this.prismaService.posts.findFirst({
      where: { id, deletedAt: null },
      select: {
        id: true,
        content: true,
        thumbnailUrl: true,
        imageUrl: true,
        menuTag: true,
        keywordTag: true,
        likeCount: true,
        viewCount: true,
        createdAt: true,
        users: {
          select: {
            id: true,
            nickname: true,
            profileImage: true,
          },
        },
        places: {
          select: {
            id: true,
            name: true,
            address: true,
            roadAddress: true,
            x: true,
            y: true,
            categories: { select: { name: true } },
          },
        },
        starRatings: {
          select: { star: true },
        },
      },
    });

    if (!postData) {
      throw new NotFoundException('해당 게시물은 존재하지 않습니다.');
    }

    const menuTagIds = postData.menuTag.split(',').map(Number);
    const menus = (
      await this.prismaService.placeMenus.findMany({
        where: {
          id: {
            in: menuTagIds,
          },
        },
        select: {
          menu: true,
        },
      })
    ).map((menu) => menu.menu);

    const result = {
      ...postData,
      menuTag: menus,
      starRatings: postData.starRatings.star,
    };
    return result;
  }

  async countPost(id: number, userId: number) {
    const findPost = await this.prismaService.posts.findFirst({ where: { id, deletedAt: null } });
    if (!findPost) {
      throw new NotFoundException('해당 게시물은 존재하지 않습니다.');
    } else if (userId !== undefined && findPost.userId === BigInt(userId))
      throw { statusCode: 200, message: '작성자는 조회수가 증가하지 않습니다.' };
    return await this.prismaService.posts.update({ where: { id }, data: { viewCount: { increment: 1 } } });
  }

  async checkPost(id: number) {
    const postData = await this.prismaService.posts.findFirst({
      where: { id, deletedAt: null },
      select: {
        content: true,
        thumbnailUrl: true,
        imageUrl: true,
        menuTag: true,
        keywordTag: true,
        placeId: true,
        ratingId: true,
        userId: true,
        createdAt: true,
        comments: { select: { userId: true } },
      },
    });
    if (!postData) {
      throw new BadRequestException('해당 게시물은 존재하지 않습니다');
    }

    const createdTime = new Date(postData.createdAt);
    const currentTime = new Date(new Date().getTime() + 9 * 60 * 60 * 1000);
    const oneDay = 24 * 60 * 60 * 1000;
    if (!(currentTime.getTime() - createdTime.getTime() < oneDay)) {
      throw new ForbiddenException('게시물은 24시간 이내에 수정이 가능해요');
    }

    const isComments = postData.comments.some((comment) => comment.userId !== postData.userId);
    if (isComments) {
      throw new HttpException('다른 사용자의 반응(댓글)이 있는 글은 수정할 수 없어요', 423);
    }

    return { message: '수정가능 합니다', postData };
  }

  async updatePost(userId: number, id: number, files: Express.Multer.File[], dto: UpdatePostRequestDto) {
    const { postData } = await this.checkPost(id);

    if (Number(userId) !== Number(postData.userId)) throw new UnauthorizedException('작성자가 아닙니다');

    let s3Upload = [];
    if (files && files.length > 0) {
      const S3URL = this.configService.get<string>('ENV_AWS_S3_URL');
      const uploadToimage = files.map(async (file) => {
        const key = `places/${id.toString()}/${Date.now()}-${Math.random().toString(16).slice(2)}${path.extname(file.originalname)}`;
        await this.s3Service.uploadS3(key, file.buffer, file.mimetype);
        return S3URL + '/' + key;
      });
      s3Upload = await Promise.all(uploadToimage);
    }

    let menuTagId = undefined;
    if (dto.menuTag && dto.menuTag.length > 0) {
      const menuData = await this.placeService.placeAddMenus(dto.menuTag, Number(postData.placeId));
      menuTagId = menuData.map((menu) => menu.id.toString()).join(',');
    }

    await this.prismaService.starRatings.update({
      where: { id: Number(postData.ratingId) },
      data: { star: dto.starRating },
    });
    await this.prismaService.posts.update({
      where: { id },
      data: {
        content: dto.content && dto.content.length > 0 ? dto.content : postData.content,
        thumbnailUrl: s3Upload.length > 0 ? s3Upload[0] : postData.thumbnailUrl,
        imageUrl: s3Upload.length > 1 ? s3Upload.slice(1).toString() : null,
        menuTag: menuTagId !== undefined ? menuTagId : postData.menuTag,
        keywordTag: dto.keywordTag && dto.keywordTag.length > 0 ? dto.keywordTag : postData.keywordTag,
      },
    });
    return { message: '수정되었습니다' };
  }

  async deletePost(userId: number, id: number) {
    const postData = await this.prismaService.posts.findFirst({ where: { id, deletedAt: null } });
    if (!postData) throw new NotFoundException('해당 게시물은 존재하지 않습니다');
    if (Number(userId) !== Number(postData.userId)) throw new UnauthorizedException('작성자가 아닙니다');
    await this.prismaService.posts.update({ where: { id }, data: { deletedAt: new Date() } });
    return { message: '삭제되었습니다' };
  }
}
