import { Controller, Body, Post, Get, Param } from '@nestjs/common';
import {
  ApiTags,
  ApiOperation,
  ApiBody,
  ApiOkResponse,
  ApiConflictResponse,
  ApiBadRequestResponse,
  ApiNotFoundResponse,
  ApiCreatedResponse,
} from '@nestjs/swagger';
import { UserService } from '../service/user.service';
import { GetUserId } from '../../../global/decorator';
import { ApiOptionGuard } from '../../../global/decorator';
import { FindPasswordRequestDto, FindSimilarResponseDto } from '../../../global/dto';

@ApiTags('User')
@Controller('users')
export class UserController {
  constructor(private readonly userService: UserService) {}

  @Post('find-password')
  @ApiOperation({ summary: '비밀번호 찾기' })
  @ApiBody({ type: FindPasswordRequestDto })
  @ApiCreatedResponse({ description: '입력하신 이메일로 임시 비밀번호를 보냈습니다.' })
  @ApiBadRequestResponse({ description: '[SocialType] 로그인으로 가입한 계정입니다.' })
  @ApiNotFoundResponse({ description: '입력하신 정보가 없습니다.' })
  async findPassword(@Body() dto: FindPasswordRequestDto) {
    await this.userService.findPassword(dto);
    return { message: '입력하신 이메일로 임시 비밀번호를 보냈습니다.', email: dto.email };
  }

  @Get('nickname/:nickname')
  @ApiOperation({ summary: '닉네임 중복확인' })
  @ApiOkResponse({ description: '사용가능한 닉네임입니다.' })
  @ApiConflictResponse({ description: '이미 사용중인 닉네임입니다.' })
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
