import { ConflictException, ForbiddenException, Injectable } from '@nestjs/common';
import { PrismaService } from '../../../global/prisma/prisma.service';
import { CreateUserDto } from '../dto/create-user.dto';
import * as bcrypt from 'bcrypt';

@Injectable()
export class AuthService {
  constructor(private readonly prismaService: PrismaService) {}

  async createUser(dto: CreateUserDto) {
    const emailCheck = await this.prismaService.emailVerifications.findFirst({
      where: { email: dto.email, isVerification: { not: true } },
    });
    if (emailCheck === null) {
      throw new ForbiddenException('이메일 인증이 완료되지 않았습니다.');
    }

    const nicknameCheck = await this.prismaService.users.findFirst({ where: { nickname: dto.nickname } });
    if (nicknameCheck) {
      throw new ConflictException('이미 사용중인 닉네임입니다.');
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
        socialType: 'LOCAL',
        role: 'USER',
        status: 'NORMAL',
        createdAt: currentTime,
        updatedAt: currentTime,
      },
    });
  }
}
