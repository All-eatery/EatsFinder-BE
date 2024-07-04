import { Module } from '@nestjs/common';
import { UserService } from './service/user.service';
import { UserController } from './controller/user.controller';
import { MailService } from '../../global/mail/mail.service';
import { PasswordMailService } from '../../global/mail/send/find-password-mail.service';

@Module({
  controllers: [UserController],
  providers: [UserService, MailService, PasswordMailService],
})
export class UserModule {}
