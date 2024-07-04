import { MailerService } from '@nestjs-modules/mailer';
import { Injectable } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';

@Injectable()
export class MailService {
  constructor(
    private readonly mailerService: MailerService,
    private readonly configService: ConfigService,
  ) {}

  async sendMail(to: string, subject: string, text: string) {
    await this.mailerService.sendMail({
      to,
      from: this.configService.get<string>('ENV_EMAIL_NAME'),
      subject,
      text,
    });
  }
}
