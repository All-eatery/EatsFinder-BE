import { ConflictException, Injectable, NotFoundException } from '@nestjs/common';
import { PrismaService } from '../../../global/prisma/prisma.service';
import { Categories } from '@prisma/client';
import { CreatePlaceRequestDto, FindLocalPlaceDto, PlacePostQueryDto } from '../../../global/dto';

@Injectable()
export class PlaceService {
  constructor(private readonly prismaService: PrismaService) {}

  async createPlace(dto: CreatePlaceRequestDto) {
    const { name, address, roadAddress, telephone, x, y, category, categoryName, categoryCode } = dto;

    const places = await this.prismaService.places.findMany({ where: { name, address } });
    const isPlaceData = places.length === 0 ? false : places;
    if (isPlaceData) throw new ConflictException('입력하신 장소가 이미 존재합니다.');

    const categoryArray = category.split('>').map((depth) => depth.trim());

    let isCategoryData = await this.prismaService.categories.findFirst({
      where: { name: categoryArray[1] },
    });
    let createCategory: Categories;
    if (!isCategoryData) {
      createCategory = await this.prismaService.categories.create({
        data: {
          name: categoryArray[1],
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
        depth1: categoryArray[0],
        depth2: categoryArray[1],
        depth3: categoryArray[2],
        depth4: categoryArray[3],
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

  async placeAddMenus(menuTag: string, placeId: number) {
    const menuArray = menuTag.split(',').map((menu) => menu.trim());

    for (const menuName of menuArray) {
      const menu = await this.prismaService.placeMenus.findFirst({ where: { menu: menuName, placeId } });

      if (!menu) {
        await this.prismaService.placeMenus.create({ data: { menu: menuName, placeId } });
      }
    }
    return await this.prismaService.placeMenus.findMany({
      where: {
        menu: {
          in: menuArray,
        },
        placeId,
      },
    });
  }

  async findLocalPlace(query: FindLocalPlaceDto, userId?: number) {
    const { pa, qa, ha, oa } = query;
    // pa: 북, qa: 남, ha: 서, oa: 동

    const findManyPlace = await this.prismaService.places.findMany({
      where: { y: { gte: qa, lte: pa }, x: { gte: ha, lte: oa } },
      select: {
        id: true,
        name: true,
        roadAddress: true,
        starRatings: { select: { star: true } },
        categories: { select: { name: true } },
        posts: { select: { thumbnailUrl: true }, orderBy: { likeCount: 'desc' } },
      },
      orderBy: { id: 'desc' },
    });

    const placeIds = findManyPlace.map((place) => place.id);

    let bookmarkedPlaceIds: number[] = [];
    if (userId) {
      const bookmarks = await this.prismaService.bookmarks.findMany({
        where: { userId },
        select: { id: true },
      });
      const bookmarkIds = bookmarks.map((b) => b.id);

      if (bookmarkIds.length > 0) {
        const bookmarkPlaces = await this.prismaService.bookmarkPlaces.findMany({
          where: {
            bookmarkId: { in: bookmarkIds },
            placeId: { in: placeIds.map((id) => BigInt(id)) },
          },
          select: { placeId: true },
        });
        bookmarkedPlaceIds = bookmarkPlaces.map((bp) => Number(bp.placeId));
      }
    }

    const results = await Promise.all(
      findManyPlace.map(async (place) => {
        const avgRating = await this.prismaService.starRatings.aggregate({
          where: { placeId: place.id },
          _avg: { star: true },
        });
        return {
          ...place,
          starRatings: avgRating._avg.star,
          bookmarkStatus: userId ? bookmarkedPlaceIds.includes(Number(place.id)) : false,
        };
      }),
    );
    return results;
  }

  async placeDetail(id: number) {
    const findOnePlace = await this.prismaService.places.findFirst({
      where: { id },
      select: {
        id: true,
        name: true,
        address: true,
        roadAddress: true,
        x: true,
        y: true,
        posts: {
          take: 1,
          orderBy: { likeCount: 'desc' },
          select: { thumbnailUrl: true, menuTag: true, keywordTag: true },
        },
      },
    });

    if (!findOnePlace) throw new NotFoundException('해당 맛집 정보는 존재하지 않습니다.');

    const post = findOnePlace.posts[0];

    if (!post) {
      const { posts, ...placeWithoutPosts } = findOnePlace;
      return { ...placeWithoutPosts };
    }

    let menuNames: string[] = [];

    if (post.menuTag) {
      const menuIds = post.menuTag
        .split(',')
        .map((id) => Number(id.trim()))
        .filter((id) => !isNaN(id));

      const menus = await this.prismaService.placeMenus.findMany({
        where: { id: { in: menuIds } },
        select: { menu: true },
      });

      menuNames = menus.map((menu) => menu.menu);
    }

    const { posts, ...placeWithoutPosts } = findOnePlace;

    return {
      ...placeWithoutPosts,
      thumbnailUrl: post.thumbnailUrl,
      keywordTag: post.keywordTag,
      menuNames,
    };
  }

  async placePosts(placeId: number, userId: number, query: PlacePostQueryDto) {
    const { cursor, sort } = query;
    const take = 10;

    const place = await this.prismaService.places.findUnique({
      where: { id: placeId },
      select: { name: true },
    });

    if (!place) throw new NotFoundException('해당 맛집의 게시물이 존재하지 않습니다.');

    let where: any = { placeId };

    if (cursor) {
      const cursorPost = await this.prismaService.posts.findUnique({
        where: { id: cursor },
        select: { createdAt: true, likeCount: true, id: true },
      });

      if (!cursorPost) {
        throw new NotFoundException('해당 맛집의 게시물이 존재하지 않습니다.');
      }

      if (sort === 'recent') {
        where.OR = [
          { createdAt: { lt: cursorPost.createdAt } },
          {
            createdAt: cursorPost.createdAt,
            id: { lt: cursorPost.id },
          },
        ];
      } else if (sort === 'like') {
        where.OR = [
          { likeCount: { lt: cursorPost.likeCount } },
          {
            likeCount: cursorPost.likeCount,
            id: { lt: cursorPost.id },
          },
        ];
      }
    }

    const posts = await this.prismaService.posts.findMany({
      where,
      orderBy: sort === 'like' ? [{ likeCount: 'desc' }, { id: 'desc' }] : [{ createdAt: 'desc' }, { id: 'desc' }],
      take,
      select: {
        id: true,
        thumbnailUrl: true,
        likeCount: true,
        users: {
          select: {
            id: true,
            nickname: true,
            profileImage: true,
          },
        },
      },
    });

    const postIds = posts.map((post) => Number(post.id));

    let likedPostIds: number[] = [];
    if (userId) {
      const likes = await this.prismaService.postLikes.findMany({
        where: {
          userId,
          postId: { in: postIds },
        },
        select: { postId: true },
      });
      likedPostIds = likes.map((like) => Number(like.postId));
    }

    const formattedPosts = posts.map((post) => ({
      id: Number(post.id),
      thumbnailUrl: post.thumbnailUrl,
      likeCount: post.likeCount,
      isLiked: likedPostIds.includes(Number(post.id)),
      nickname: post.users.nickname,
      profileImage: post.users.profileImage,
    }));

    return {
      pagination: {
        totalItems: formattedPosts.length,
        itemsPerPage: take,
      },
      items: formattedPosts,
      lastItemId: formattedPosts.length > 0 ? formattedPosts[formattedPosts.length - 1].id : null,
    };
  }
}
