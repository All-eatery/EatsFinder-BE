import { ConflictException, Injectable } from '@nestjs/common';
import { PrismaService } from '../../../global/prisma/prisma.service';
import { Categories } from '@prisma/client';
import { CreatePlaceRequestDto } from '../../../global/dto';

@Injectable()
export class PlaceService {
  constructor(private readonly prismaService: PrismaService) {}

  async createPlace(dto: CreatePlaceRequestDto) {
    const { name, address, roadAddress, telephone, x, y, category, categoryName, categoryCode } = dto;

    const places = await this.prismaService.places.findMany({ where: { name, address } });
    const isPlaceData = places.length === 0 ? false : places;
    if (isPlaceData) throw new ConflictException('입력하신 장소가 이미 존재합니다.');

    let isCategoryData = await this.prismaService.categories.findFirst({
      where: { name: category.split('>')[1].trim() },
    });
    let createCategory: Categories;
    if (!isCategoryData) {
      createCategory = await this.prismaService.categories.create({
        data: {
          name: category.split('>')[1].trim(),
          type: categoryName,
          code: categoryCode,
        },
      });
      isCategoryData = createCategory;
    }

    return await this.prismaService.places.create({
      data: {
        name,
        address,
        roadAddress,
        telephone,
        x,
        y,
        categoryId: isCategoryData.id,
      },
    });
  }

  async findPlace(name: string) {
    const findManyPlace = await this.prismaService.places.findMany({
      where: {
        name: {
          contains: name,
        },
      },
      select: {
        id: true,
        name: true,
        roadAddress: true,
        posts: {
          select: {
            thumbnailUrl: true,
            likeCount: true,
          },
          orderBy: {
            likeCount: 'desc',
          },
        },
      },
      orderBy: {
        id: 'desc',
      },
    });
    return findManyPlace.map((place) => ({
      id: Number(place.id),
      name: place.name,
      roadAddress: place.roadAddress,
      thumbnailUrl: place.posts.length > 0 ? place.posts[0].thumbnailUrl : null,
    }));
  }
}
