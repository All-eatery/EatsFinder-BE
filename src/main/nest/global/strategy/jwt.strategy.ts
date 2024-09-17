import { Injectable, UnauthorizedException } from '@nestjs/common';
import { PassportStrategy } from '@nestjs/passport';
import { ExtractJwt, Strategy } from 'passport-jwt';
import { ConfigService } from '@nestjs/config';
import { AuthService } from '../../domain/auth/service/auth.service';

@Injectable()
export class JwtStrategy extends PassportStrategy(Strategy) {
  constructor(
    private readonly authService: AuthService,
    private readonly configService: ConfigService,
  ) {
    super({
      jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
      secretOrKey: configService.get<string>('ENV_JWT_SECRET'),
    });
  }

  async validate(payload: any) {
    const { userId } = payload;
    const user = await this.authService.getUser(Number(userId));
    if (!user) {
      throw new UnauthorizedException();
    }
    return user;
  }
}
