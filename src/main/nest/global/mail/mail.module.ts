import { Module } from '@nestjs/common';
import { MailService } from './mail.service';
import { MailerModule } from '@nestjs-modules/mailer';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { PasswordMailService } from './send/find-password-mail.service';

@Module({
  imports: [
    ConfigModule,
    MailerModule.forRootAsync({
      imports: [ConfigModule],
      inject: [ConfigService],
      useFactory: async (configService: ConfigService) => ({
        transport: {
          host: 'smtp.gmail.com',
          port: 587,
          secure: false,
          auth: {
            user: configService.get<string>('ENV_EMAIL_NAME'),
            pass: configService.get<string>('ENV_EMAIL_PASSWORD'),
          },
        },
        defaults: {
          from: '"잇츠파인더" <eatsfinder24@gmail.com>',
        },
      }),
    }),
  ],
  providers: [MailService, PasswordMailService],
  exports: [MailService],
})
export class MailModule {}
