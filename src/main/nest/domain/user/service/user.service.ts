import { BadRequestException, ConflictException, Injectable, NotFoundException } from '@nestjs/common';
import { PrismaService } from '../../../global/prisma/prisma.service';
import { PasswordMailService } from '../../../global/mail/send/find-password-mail.service';
import * as bcrypt from 'bcrypt';
import { FindPasswordRequestDto } from '../../../global/dto';

@Injectable()
export class UserService {
  constructor(
    private readonly prismaService: PrismaService,
    private readonly passwordMailService: PasswordMailService,
  ) {}

  async findPassword(dto: FindPasswordRequestDto) {
    const { name, email } = dto;
    const isUserData = await this.prismaService.users.findFirst({ where: { name, email } });

    if (!isUserData) {
      throw new NotFoundException('입력하신 정보가 없습니다.');
    }
    if (isUserData.socialType !== 'LOCAL') {
      throw new BadRequestException(`${isUserData.socialType} 로그인으로 가입한 계정입니다.`);
    }

    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=';
    let generateRandom = '';
    for (let i = 0; i < 12; i++) {
      generateRandom += characters.charAt(Math.floor(Math.random() * characters.length));
    }

    this.passwordMailService.createUserPasswordMail(isUserData, generateRandom);

    const hashPassword = await bcrypt.hash(generateRandom, 11);

    return await this.prismaService.users.update({
      where: { id: isUserData.id },
      data: { password: hashPassword },
    });
  }

  async checkNickname(nickname: string) {
    const nicknameCheck = await this.prismaService.users.findFirst({ where: { nickname } });
    if (nicknameCheck) {
      throw new ConflictException('이미 사용중인 닉네임입니다.');
    }
    return;
  }

  async findSimilar(userId: number) {
    // 1. 사용자가 좋아요를 누른 게시물의 카테고리 ID를 가져와서 가장 빈도가 높은 카테고리 ID를 찾음
    const postLikes = await this.prismaService.postLikes.findMany({
      where: { userId },
      select: { posts: { select: { places: { select: { categories: { select: { id: true } } } } } } },
    });

    const idCounts = postLikes.reduce<Record<number, number>>((acc, postLike) => {
      const categoryId = Number(postLike.posts.places.categories.id);
      acc[categoryId] = (acc[categoryId] || 0) + 1;
      return acc;
    }, {});

    const maxCategoryId = Object.entries(idCounts).reduce(
      (maxId, [id, count]) => (Number(count) > idCounts[maxId] ? Number(id) : maxId),
      Number(Object.keys(idCounts)[0] || 0),
    );

    // 2. 해당 카테고리 ID로 관련된 장소를 찾음
    const places = await this.prismaService.places.findMany({
      where: { categories: { id: maxCategoryId } },
      select: { id: true },
    });
    const placeIds = places.map((place) => place.id);

    // 3. 장소 ID로 관련된 게시물들을 찾음
    const posts = await this.prismaService.posts.findMany({
      where: { placeId: { in: placeIds } },
      select: { id: true },
    });
    const postIds = posts.map((post) => post.id);

    // 4. 게시물 ID로 좋아요를 누른 사용자들의 정보를 찾되, 현재 사용자와 다른 사용자만
    const postLikesWithSamePosts = await this.prismaService.postLikes.findMany({
      where: {
        postId: { in: postIds },
        userId: { not: userId },
      },
      select: {
        postId: true,
        userId: true,
      },
    });
    const uniqueUserIds = [...new Set(postLikesWithSamePosts.map((like) => like.userId))];

    // 5. 같은 게시물 ID를 가진 사용자들의 게시물 수를 계산
    const postCountsByUser = await this.prismaService.posts.groupBy({
      by: ['userId'],
      _count: { _all: true },
      where: { userId: { in: uniqueUserIds } },
    });

    const userPostCounts: Record<number, number> = {};
    postCountsByUser.forEach((user) => {
      userPostCounts[Number(user.userId)] = Number(user._count._all);
    });

    // 6. 사용자 정보를 가져오고, 각 사용자에 대해 게시물 수를 추가
    const findUserData = await this.prismaService.users.findMany({
      where: { id: { in: uniqueUserIds, not: userId } },
      select: {
        id: true,
        nickname: true,
        profileImage: true,
        followerCount: true,
      },
      orderBy: { id: 'desc' },
    });

    // 7. 현재 사용자가 팔로우하는 사용자 목록 가져오기
    const following = await this.prismaService.follows.findMany({
      where: { followedUserId: userId },
      select: { followingUserId: true },
    });
    const followingUserIds = following.map((follow) => follow.followingUserId);

    return findUserData.map((user) => ({
      ...user,
      postCount: userPostCounts[Number(user.id)] || 0,
      followerStatus: followingUserIds.includes(user.id),
    }));
  }
}
