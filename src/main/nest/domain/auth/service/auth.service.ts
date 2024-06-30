import { Injectable } from '@nestjs/common';
import { PrismaService } from '../../../global/prisma/prisma.service';
import { CreateUserDto } from '../dto/create-user.dto';

@Injectable()
export class AuthService {
  constructor(private readonly prismaService: PrismaService) {}

  async createUser(dto: CreateUserDto) {
    const currentTime = new Date();

    return await this.prismaService.users.create({
      data: {
        email: dto.email,
        name: dto.name,
        nickname: dto.nickname,
        password: dto.password,
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
