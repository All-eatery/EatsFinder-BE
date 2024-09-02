import { Controller, Body, Post, Get, Param } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiBody, ApiOkResponse } from '@nestjs/swagger';
import { UserService } from '../service/user.service';
import { GetUserId } from '../../../global/decorator';
import { ApiOptionGuard } from '../../../global/decorator/api-guard-optional.decorator';
import { FindPasswordRequestDto, FindSimilarResponseDto } from '../../../global/dto';

@ApiTags('User')
@Controller('users')
export class UserController {
  constructor(private readonly userService: UserService) {}

  @Post('find-password')
  @ApiOperation({ summary: '비밀번호 찾기' })
  @ApiBody({ type: FindPasswordRequestDto })
  async findPassword(@Body() dto: FindPasswordRequestDto) {
    await this.userService.findPassword(dto);
    return { message: '입력하신 이메일로 임시 비밀번호를 보냈습니다.', email: dto.email };
  }

  @Get('nickname/:nickname')
  async checkNickname(@Param('nickname') nickname: string) {
    await this.userService.checkNickname(nickname);
    return { message: '사용가능한 닉네임입니다.' };
  }

  @Get('similar')
  @ApiOptionGuard()
  @ApiOperation({ summary: '취향 비슷한 유저' })
  @ApiOkResponse({ type: [FindSimilarResponseDto] })
  async findSimilar(@GetUserId() userId: number) {
    if (userId === undefined) return undefined;
    return await this.userService.findSimilar(userId);
  }
}
