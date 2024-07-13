import { Injectable, NotFoundException } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { PrismaService } from '../../../global/prisma/prisma.service';
import { S3Service } from '../../../global/s3/s3.service';
import { CreatePostRequestDto } from '../../../global/dto';
import * as path from 'path';

@Injectable()
export class PostService {
  constructor(
    private readonly configService: ConfigService,
    private readonly prismaService: PrismaService,
    private readonly s3Service: S3Service,
  ) {}

  async createPost(files: Express.Multer.File[], dto: CreatePostRequestDto) {
    const S3URL = this.configService.get<string>('ENV_AWS_S3_URL');
    const { content, menuTag, keywordTag, starRating, placeId } = dto;

    const isPlaceData = await this.prismaService.places.findFirst({ where: { id: placeId } });
    if (!isPlaceData) throw new NotFoundException('잘못된 장소입니다.');

    const uploadToimage = files.map(async (file) => {
      const key = `places/${isPlaceData.id.toString()}/${Date.now()}-${Math.random().toString(16).slice(2)}${path.extname(file.originalname)}`;
      await this.s3Service.uploadS3(key, file.buffer, file.mimetype);
      return S3URL + key;
    });
    const s3Upload = await Promise.all(uploadToimage);

    //Todo userId 임시 구현
    const userId: number = 1;
    const createRate = await this.prismaService.starRatings.create({ data: { star: starRating } });
    return await this.prismaService.posts.create({
      data: {
        content,
        thumbnailUrl: s3Upload[0],
        imageUrl: s3Upload.length > 1 ? s3Upload.slice(1).toString() : null,
        menuTag,
        keywordTag,
        userId,
        placeId,
        ratingId: createRate.id,
      },
    });
  }
}
