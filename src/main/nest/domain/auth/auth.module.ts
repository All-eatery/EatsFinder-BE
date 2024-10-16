import { Module } from '@nestjs/common';
import { AuthController } from './controller/auth.controller';
import { AuthService } from './service/auth.service';
import { PassportModule } from '@nestjs/passport';
import { JwtModule } from '@nestjs/jwt';
import { LocalStrategy } from '../../global/strategy/local.strategy';
import { JwtStrategy } from '../../global/strategy/jwt.strategy';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { NaverStrategy } from '../../global/strategy/naver.strategy';
import { NaverAuthGuard } from '../../global/guard/naver-auth.guard';

@Module({
  imports: [
    ConfigModule,
    PassportModule,
    JwtModule.registerAsync({
      imports: [ConfigModule],
      useFactory: async (configService: ConfigService) => ({
        secret: configService.get<string>('ENV_JWT_SECRET'),
        signOptions: { expiresIn: `${configService.get<string>('ENV_JWT_EXPIRATION_TIME')}d` },
      }),
      inject: [ConfigService],
    }),
  ],
  providers: [AuthService, LocalStrategy, JwtStrategy, NaverStrategy, NaverAuthGuard],
  controllers: [AuthController],
})
export class AuthModule {}
