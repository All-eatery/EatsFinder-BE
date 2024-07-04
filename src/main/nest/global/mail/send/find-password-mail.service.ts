import { Injectable } from '@nestjs/common';
import { MailService } from '../mail.service';

@Injectable()
export class PasswordMailService {
  constructor(private readonly mailService: MailService) {}

  //* 임시 비밀번호 메일 발송
  createUserPasswordMail(isUserData: any, generateRandom: string) {
    const subject = '[잇츠파인더] 요청하신 회원정보 찾기 안내 메일입니다.';
    this.mailService.sendPassword(isUserData.email, subject, isUserData.name, generateRandom);
  }
}
