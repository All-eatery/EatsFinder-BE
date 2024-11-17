import { BadRequestException, Injectable } from '@nestjs/common';
import { CreateBookmarkListRequestDto } from '../../../global/dto';
import { PrismaService } from '../../../global/prisma/prisma.service';

@Injectable()
export class BookmarkService {
  constructor(private readonly prismaService: PrismaService) {}

  async create(userId: number, dto: CreateBookmarkListRequestDto) {
    const totalBookmark = await this.prismaService.bookmarks.count({ where: { userId } });
    if (totalBookmark > 99) throw new BadRequestException('생성할 수 있는 최대 리스트 개수는 100개까지입니다.');
    return await this.prismaService.bookmarks.create({ data: { title: dto.listname, userId } });
  }

  async find(userId: number, cursor: number) {
    const LIMIT = 10;
    let bookmark: any;

    if (!cursor) {
      bookmark = await this.prismaService.bookmarks.findMany({
        take: LIMIT,
        where: { userId },
        select: {
          id: true,
          title: true,
          count: true,
          bookmarkPlaces: {
            select: {
              places: {
                select: {
                  posts: {
                    select: { thumbnailUrl: true },
                    orderBy: { likeCount: 'desc' },
                    take: 1,
                  },
                },
              },
            },
          },
        },
      });
    } else {
      bookmark = await this.prismaService.bookmarks.findMany({
        take: LIMIT,
        skip: 1,
        cursor: { id: cursor },
        where: { userId },
        select: {
          id: true,
          title: true,
          count: true,
          bookmarkPlaces: {
            select: {
              places: {
                select: {
                  posts: {
                    select: { thumbnailUrl: true },
                    orderBy: { likeCount: 'desc' },
                    take: 1,
                  },
                },
              },
            },
          },
        },
      });
    }

    const bookmarkData = bookmark.map((list: { bookmarkPlaces: { places: { posts: any } }[] }) => ({
      ...list,
      bookmarkPlaces:
        list.bookmarkPlaces.length === 0
          ? null
          : list.bookmarkPlaces
              .flatMap((place: { places: { posts: any } }) => place.places.posts)
              .map((post: { thumbnailUrl: string }) => ({ thumbnailUrl: post.thumbnailUrl })),
    }));
    const totalItems = await this.prismaService.bookmarks.count({ where: { userId } });

    return {
      pagination: { totalItems, itemsPerPage: bookmarkData.length },
      items: bookmarkData,
      lastItemId: bookmarkData.length > 0 ? bookmarkData[bookmarkData.length - 1].id : null,
    };
  }

  findOne(id: number) {
    return `This action returns a #${id} bookmark`;
  }

  update(id: number) {
    return `This action updates a #${id} bookmark`;
  }

  remove(id: number) {
    return `This action removes a #${id} bookmark`;
  }
}
