import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { PrismaModule } from '../global/prisma/prisma.module';
import { AuthModule } from '../domain/auth/auth.module';
import { UserModule } from '../domain/user/user.module';
import { MailModule } from '../global/mail/mail.module';
import { PostModule } from '../domain/post/post.module';
import { PlaceModule } from '../domain/place/place.module';

@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true }),
    PrismaModule,
    MailModule,
    AuthModule,
    UserModule,
    PostModule,
    PlaceModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
