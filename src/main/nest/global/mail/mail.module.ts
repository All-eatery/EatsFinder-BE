import { Module } from '@nestjs/common';
import { MailService } from './mail.service';
import { MailerModule } from '@nestjs-modules/mailer';
import { HandlebarsAdapter } from '@nestjs-modules/mailer/dist/adapters/handlebars.adapter';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { PasswordMailService } from './send/find-password-mail.service';
import { join } from 'path';

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
        template: {
          dir: join(__dirname, '..', 'mail', 'templates'),
          adapter: new HandlebarsAdapter(),
          options: {
            strict: true,
          },
        },
      }),
    }),
  ],
  providers: [MailService, PasswordMailService],
  exports: [MailService],
})
export class MailModule {}
