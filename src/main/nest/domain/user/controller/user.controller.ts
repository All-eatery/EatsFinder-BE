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
    return this.userService.findPassword(findPasswordDto);
  }
}
