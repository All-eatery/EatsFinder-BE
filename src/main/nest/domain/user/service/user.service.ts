import { BadRequestException, Injectable, NotFoundException } from '@nestjs/common';
import { PrismaService } from '../../../global/prisma/prisma.service';
import { FindPasswordDto } from '../dto/find-password.dto';
import { PasswordMailService } from '../../../global/mail/send/find-password-mail.service';
import * as bcrypt from 'bcrypt';

@Injectable()
export class UserService {
  constructor(
    private readonly prismaService: PrismaService,
    private readonly passwordMailService: PasswordMailService,
  ) {}

  async findPassword(findPasswordDto: FindPasswordDto) {
    const { name, email } = findPasswordDto;
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
}
