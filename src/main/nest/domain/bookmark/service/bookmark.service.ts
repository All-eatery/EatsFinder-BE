import {
  BadRequestException,
  ConflictException,
  Injectable,
  NotFoundException,
  UnauthorizedException,
} from '@nestjs/common';
import {
  CreateBookmarkListRequestDto,
  CreateBookmarkPlaceDto,
  RemoveBookmarkPlaceDto,
  UpdateBookmarkListRequestDto,
  UpdateBookmarkPlaceRequestDto,
} from '../../../global/dto';
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

  async update(id: number, userId: number, dto: UpdateBookmarkListRequestDto) {
    const bookmarkData = await this.prismaService.bookmarks.findFirst({ where: { id } });
    if (!bookmarkData) throw new BadRequestException('해당 리스트는 존재하지 않습니다.');
    if (Number(userId) !== Number(bookmarkData.userId)) {
      throw new UnauthorizedException('본인 리스트만 수정할 수 있습니다.');
    }
    await this.prismaService.bookmarks.update({ where: { id }, data: { title: dto.title } });
    return { message: '수정되었습니다.' };
  }

  async remove(id: number, userId: number) {
    const bookmarkData = await this.prismaService.bookmarks.findFirst({ where: { id } });
    if (!bookmarkData) throw new BadRequestException('해당 리스트는 존재하지 않습니다.');
    if (Number(userId) !== Number(bookmarkData.userId)) {
      throw new UnauthorizedException('본인 리스트만 삭제할 수 있습니다.');
    }
    await this.prismaService.bookmarks.delete({ where: { id } });
    return { message: '삭제되었습니다.' };
  }

  async createBookmark(userId: number, dto: CreateBookmarkPlaceDto) {
    const { place, lists } = dto;

    const bookmarkListData = await this.prismaService.bookmarks.findMany({ where: { userId, id: { in: lists } } });

    if (bookmarkListData.length === 0) throw new NotFoundException('리스트가 존재하지 않습니다.');
    if (bookmarkListData.length !== lists.length) throw new BadRequestException('리스트에 추가할 수 없습니다.');

    const isBookmarkPlaces = await this.prismaService.bookmarkPlaces.findMany({
      where: { placeId: place, bookmarkId: { in: lists } },
    });
    const listBigInt = lists.map(BigInt);

    const existingBookmarkPlaces = isBookmarkPlaces.map((place) => place.bookmarkId);
    const newLists = listBigInt.filter((list) => !existingBookmarkPlaces.includes(list));

    if (newLists.length === 0) throw new ConflictException('이미 추가된 맛집입니다.');

    await this.prismaService.bookmarkPlaces.createMany({
      data: newLists.map((list) => ({
        placeId: place,
        bookmarkId: list,
      })),
    });

    return await this.prismaService.bookmarks.updateMany({
      where: { id: { in: newLists } },
      data: { count: { increment: 1 } },
    });
  }

  async findBookmark(userId: number, id: number, cursor: number) {
    const bookmarkData = await this.prismaService.bookmarks.findFirst({ where: { userId, id } });
    if (bookmarkData === null) throw new NotFoundException('리스트가 존재하지 않습니다.');

    const LIMIT = 10;
    let bookmarkPlaceData: any;

    if (!cursor) {
      bookmarkPlaceData = await this.prismaService.bookmarkPlaces.findMany({
        take: LIMIT,
        where: { bookmarkId: id },
        select: {
          id: true,
          places: {
            select: {
              id: true,
              name: true,
              roadAddress: true,
              depth2: true,
              posts: {
                select: { thumbnailUrl: true },
                orderBy: { likeCount: 'desc' },
                take: 1,
              },
            },
          },
        },
      });
    } else {
      bookmarkPlaceData = await this.prismaService.bookmarkPlaces.findMany({
        take: LIMIT,
        skip: 1,
        cursor: { id: cursor },
        where: { bookmarkId: id },
        select: {
          id: true,
          places: {
            select: {
              id: true,
              name: true,
              roadAddress: true,
              depth2: true,
              posts: {
                select: { thumbnailUrl: true },
                orderBy: { likeCount: 'desc' },
                take: 1,
              },
            },
          },
        },
      });
    }

    bookmarkPlaceData = bookmarkPlaceData.map((bookmarkPlace: any) => ({
      ...bookmarkPlace,
      places: {
        ...bookmarkPlace.places,
        thumbnailUrl: bookmarkPlace.places.posts.length > 0 ? bookmarkPlace.places.posts[0].thumbnailUrl || null : null,
        posts: undefined,
      },
    }));

    const totalItems = await this.prismaService.bookmarkPlaces.count({ where: { bookmarkId: id } });

    return {
      pagination: { totalItems, itemsPerPage: bookmarkPlaceData.length },
      items: bookmarkPlaceData,
      lastItemId: bookmarkPlaceData.length > 0 ? bookmarkPlaceData[bookmarkPlaceData.length - 1].id : null,
    };
  }

  async updateBookmark(userId: number, dto: UpdateBookmarkPlaceRequestDto) {
    const { places, lists } = dto;

    const bookmarkListData = await this.prismaService.bookmarks.findMany({
      where: { userId, id: { in: lists } },
      select: { id: true },
    });

    if (bookmarkListData.length === 0) throw new NotFoundException('리스트가 존재하지 않습니다.');
    if (bookmarkListData.length !== lists.length) throw new BadRequestException('리스트에 이동할 수 없습니다.');

    const existingBookmarkPlaces = await this.prismaService.bookmarkPlaces.findMany({
      where: { bookmarkId: { in: lists } },
      select: { placeId: true, bookmarkId: true },
    });

    const existingPlacesByBookmark = lists.reduce(
      (acc, listId) => {
        acc[listId] = existingBookmarkPlaces
          .filter((entry) => Number(entry.bookmarkId) === listId)
          .map((entry) => Number(entry.placeId));
        return acc;
      },
      {} as Record<number, number[]>,
    );

    await this.prismaService.bookmarkPlaces.deleteMany({
      where: {
        bookmarkId: { in: lists.map(BigInt) },
        placeId: { notIn: places.map(BigInt) },
      },
    });

    const newBookmarkPlaces = lists.flatMap((listId) => {
      const existingPlaces = existingPlacesByBookmark[listId] || [];
      const placesToAdd = places.filter((place) => !existingPlaces.includes(place));
      return placesToAdd.map((placeId) => ({ bookmarkId: BigInt(listId), placeId: BigInt(placeId) }));
    });

    if (newBookmarkPlaces.length > 0) {
      await this.prismaService.bookmarkPlaces.createMany({ data: newBookmarkPlaces, skipDuplicates: true });
    }

    return { message: '맛집이 수정되었습니다.' };
  }
}
