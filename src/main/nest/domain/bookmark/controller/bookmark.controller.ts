import { Controller, Get, Post, Patch, Param, Delete, Body, Query, ParseIntPipe } from '@nestjs/common';
import {
  ApiBadRequestResponse,
  ApiCreatedResponse,
  ApiOkResponse,
  ApiOperation,
  ApiQuery,
  ApiTags,
  ApiUnauthorizedResponse,
} from '@nestjs/swagger';
import { BookmarkService } from '../service/bookmark.service';
import { ApiGuard, GetUserId } from '../../../global/decorator';
import {
  CreateBookmarkListRequestDto,
  FindAllBookmarkListDto,
  FindAllBookmarkListResponseDto,
  UpdateBookmarkListRequestDto,
} from '../../../global/dto';
import { Bookmarks } from '@prisma/client';

@ApiTags('Bookmark')
@Controller('bookmarks')
export class BookmarkController {
  constructor(private readonly bookmarkService: BookmarkService) {}

  @Post('lists')
  @ApiGuard()
  @ApiOperation({ summary: '리스트 생성' })
  @ApiCreatedResponse({ description: '리스트가 생성되었습니다.' })
  @ApiBadRequestResponse({ description: '생성할 수 있는 최대 리스트 개수는 100개까지입니다.' })
  async create(@GetUserId() userId: number, @Body() dto: CreateBookmarkListRequestDto) {
    const bookmark: Bookmarks = await this.bookmarkService.create(userId, dto);
    return { message: '리스트가 생성되었습니다.', bookmark };
  }

  @Get('lists')
  @ApiGuard()
  @ApiQuery({ name: 'cursor', required: false })
  @ApiOperation({ summary: '리스트 조회' })
  @ApiOkResponse({ type: FindAllBookmarkListResponseDto })
  async find(@GetUserId() userId: number, @Query() query: FindAllBookmarkListDto) {
    return await this.bookmarkService.find(userId, query.cursor);
  }

  @Patch('lists/:id')
  @ApiGuard()
  @ApiOperation({ summary: '리스트 이름 수정' })
  @ApiOkResponse({ description: '수정되었습니다.' })
  @ApiBadRequestResponse({ description: '해당 리스트는 존재하지 않습니다.' })
  @ApiUnauthorizedResponse({ description: '본인 리스트만 수정할 수 있습니다.' })
  async update(
    @Param('id', ParseIntPipe) id: number,
    @GetUserId() userId: number,
    @Body() dto: UpdateBookmarkListRequestDto,
  ) {
    return await this.bookmarkService.update(id, userId, dto);
  }

  @Delete(':id')
  remove(@Param('id') id: string) {
    return this.bookmarkService.remove(+id);
  }
}
