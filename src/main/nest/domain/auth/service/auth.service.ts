import { Injectable } from '@nestjs/common';
import { PrismaService } from '../../../global/prisma/prisma.service';
import { CreateUserDto } from '../dto/create-user.dto';
import * as bcrypt from 'bcrypt';

@Injectable()
export class AuthService {
  constructor(private readonly prismaService: PrismaService) {}

  async createUser(dto: CreateUserDto) {
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
