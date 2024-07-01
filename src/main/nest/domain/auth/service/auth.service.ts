import { ConflictException, ForbiddenException, Injectable, UnauthorizedException } from '@nestjs/common';
import { PrismaService } from '../../../global/prisma/prisma.service';
import { CreateUserDto } from '../dto/create-user.dto';
import * as bcrypt from 'bcrypt';
import { JwtService } from '@nestjs/jwt';

@Injectable()
export class AuthService {
  constructor(
    private readonly prismaService: PrismaService,
    private readonly jwtService: JwtService,
  ) {}

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

  async validateUser(email: string, password: string) {
    const user = await this.prismaService.users.findFirst({ where: { email, socialType: 'LOCAL' } });
    if (user && (await bcrypt.compare(password, user.password))) {
      const { password, ...result } = user;
      return result;
    }
    throw new UnauthorizedException('잘못된 접근입니다.');
  }

  async login(user: any) {
    console.log('🚀  user:', user);
    const payload = { userId: user.id.toString(), role: user.role };
    return {
      accessToken: this.jwtService.sign(payload),
    };
  }
}
