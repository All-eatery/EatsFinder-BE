import { MailerService } from '@nestjs-modules/mailer';
import { Injectable } from '@nestjs/common';

@Injectable()
export class MailService {
  constructor(private readonly mailerService: MailerService) {}

  async sendPassword(email: string, subject: string, username: string, newPassword: string) {
    await this.mailerService.sendMail({
      to: email,
      subject,
      template: './password-reset',
      context: {
        username,
        newPassword,
        currentDate: new Date().toLocaleString('ko-KR'),
      },
    });
  }
}
