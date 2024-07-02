import { Strategy } from 'passport-local';
import { PassportStrategy } from '@nestjs/passport';
import { Injectable, UnauthorizedException } from '@nestjs/common';
import { AuthService } from '../service/auth.service';
import { UsersSocialType } from '@prisma/client';

@Injectable()
export class LocalStrategy extends PassportStrategy(Strategy) {
  constructor(private authService: AuthService) {
    super({ usernameField: 'email', session: false });
  }

  async validate(email: string, password: string): Promise<any> {
    const socialType: UsersSocialType = 'LOCAL';
    const user = await this.authService.validateUser(email, password, socialType);
    if (!user) {
      throw new UnauthorizedException('잘못된 접근입니다.');
    }
    return user;
  }
}
