import { Injectable, NotFoundException } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { PrismaService } from '../../../global/prisma/prisma.service';
import { S3Service } from '../../../global/s3/s3.service';
import { CreatePostRequestDto } from '../../../global/dto';
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
      return S3URL + key;
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
      where: { id },
      select: {
        id: true,
        content: true,
        thumbnailUrl: true,
        imageUrl: true,
        menuTag: true,
        keywordTag: true,
        likeCount: true,
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
}
