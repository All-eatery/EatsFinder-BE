import { Controller, UseGuards, Body, Req, Get, Post } from '@nestjs/common';
import { ApiTags, ApiBody, ApiOperation, ApiResponse } from '@nestjs/swagger';
import { UsersSocialType } from '@prisma/client';
import { AuthService } from '../service/auth.service';
import { CreateUserDto } from '../dto/create-user.dto';
import { LoginUserDto } from '../dto/login-user.dto';
import { LocalAuthGuard } from '../guard/local-auth.guard';

@ApiTags('Auth')
@Controller('auth')
export class AuthController {
  constructor(private readonly authService: AuthService) {}

  @Post('signup')
  @ApiOperation({ summary: '로컬 회원 가입' })
  @ApiBody({ type: CreateUserDto })
  @ApiResponse({ status: 201, description: '회원 가입 성공' })
  async createUser(@Body() dto: CreateUserDto) {
    const socialType: UsersSocialType = 'LOCAL';
    return await this.authService.createUser(dto, socialType);
  }

  @Post('login')
  @ApiOperation({ summary: '로컬 로그인' })
  @ApiBody({ type: LoginUserDto })
  @ApiResponse({ status: 200, description: '로그인 성공' })
  @UseGuards(LocalAuthGuard)
  async login(@Req() req: any) {
    return this.authService.login(req.user);
  }
}
