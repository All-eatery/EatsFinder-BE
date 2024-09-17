import { PassportStrategy } from '@nestjs/passport';
import { Strategy } from 'n-passport-naver';
import { Injectable } from '@nestjs/common';
import { AuthService } from '../../domain/auth/service/auth.service';
import { ConfigService } from '@nestjs/config';

@Injectable()
export class NaverStrategy extends PassportStrategy(Strategy, 'naver') {
  constructor(
    private authService: AuthService,
    private readonly configService: ConfigService,
  ) {
    super({
      clientID: configService.get<string>('ENV_NAVER_CLIENT_ID'),
      clientSecret: configService.get<string>('ENV_NAVER_CLIENT_SECRET'),
      callbackURL: configService.get<string>('ENV_NAVER_REDIRECT_URI'),
    });
  }

  async validate(accessToken: string, refreshToken: string, profile: any, done: any): Promise<any> {
    const { provider, _json } = profile;
    console.log('🚀  _json:', _json);

    const userData = {
      name: _json.name,
      phoneNumber: _json.mobile_e164,
      email: _json.email,
      password: _json.id,
      nickname: _json.nickname,
      profileImage: _json.profile_image,
    };

    const user = await this.authService.validateUser(_json.email, _json.id, provider.toUpperCase());

    if (!user) {
      return await this.authService.createUser(userData, provider.toUpperCase());
    }
    return user;
  }
}
