import { Controller, Body, Post } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiBody } from '@nestjs/swagger';
import { UserService } from '../service/user.service';
import { FindPasswordDto } from '../dto/find-password.dto';

@ApiTags('User')
@Controller('user')
export class UserController {
  constructor(private readonly userService: UserService) {}

  @Post('find-password')
  @ApiOperation({ summary: '비밀번호 찾기' })
  @ApiBody({ type: FindPasswordDto })
  async findPassword(@Body() findPasswordDto: FindPasswordDto) {
    await this.userService.findPassword(findPasswordDto);
    return { message: '입력하신 이메일로 임시 비밀번호를 보냈습니다.', email: findPasswordDto.email };
  }
}
