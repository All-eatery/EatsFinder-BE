import { ConflictException, ForbiddenException, Injectable, UnauthorizedException } from '@nestjs/common';
import { PrismaService } from '../../../global/prisma/prisma.service';
import { CreateUserDto } from '../dto/create-user.dto';
import * as bcrypt from 'bcrypt';
import { JwtService } from '@nestjs/jwt';
import { UsersSocialType } from '@prisma/client';

@Injectable()
export class AuthService {
  constructor(
    private readonly prismaService: PrismaService,
    private readonly jwtService: JwtService,
  ) {}

  async createUser(dto: CreateUserDto, socialType: UsersSocialType) {
    console.log('🚀  dto: ', dto);
    const nicknameCheck = await this.prismaService.users.findFirst({ where: { nickname: dto.nickname } });
    switch (socialType) {
      case 'LOCAL':
        // LOCAL 일때
        const localUserData = await this.prismaService.users.findFirst({ where: { email: dto.email, socialType } });
        if (localUserData) {
          throw new ConflictException('이미 사용중인 이메일입니다.');
        }
        if (nicknameCheck) {
          throw new ConflictException('이미 사용중인 닉네임입니다.');
        }

        const emailCheck = await this.prismaService.emailVerifications.findFirst({
          where: { email: dto.email, complete: { not: false } },
        });
        if (emailCheck === null) {
          throw new ForbiddenException('이메일 인증이 완료되지 않았습니다.');
        }
        break;
      case 'NAVER':
        // NAVER 일때
        if (nicknameCheck || nicknameCheck?.nickname === dto.nickname) {
          dto.nickname = dto.nickname + `-${socialType}`;
        }
        break;
    }

    const currentTime = new Date();
    const hashPassword = await bcrypt.hash(dto.password, 11);

    return await this.prismaService.users.create({
      data: {
        email: dto.email,
        name: dto.name,
        nickname: dto.nickname,
        password: hashPassword,
        phoneNumber: dto.phoneNumber,
        socialType,
        role: 'USER',
        status: 'NORMAL',
        createdAt: currentTime,
        updatedAt: currentTime,
      },
    });
  }

  async validateUser(email: string, password: string, socialType: UsersSocialType) {
    const user = await this.prismaService.users.findFirst({ where: { email, socialType: socialType } });
    if (user && (await bcrypt.compare(password, user.password))) {
      const { password, ...result } = user;
      return result;
    }
    return;
  }

  async login(user: any) {
    const payload = { userId: user.id.toString(), role: user.role };
    return {
      accessToken: this.jwtService.sign(payload),
    };
  }

  async getUser(id: number) {
    const result = await this.prismaService.users.findFirst({ where: { id } });
    const { password, ...user } = result;
    return user;
  }
}
