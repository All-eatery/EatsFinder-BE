import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { PrismaModule } from '../global/prisma/prisma.module';
import { AuthModule } from '../domain/auth/auth.module';
import { MailModule } from '../global/mail/mail.module';

@Module({
  imports: [ConfigModule.forRoot({ isGlobal: true }), PrismaModule, MailModule, AuthModule],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
