import { Controller, Get, Post, Patch, Param, Delete, Body } from '@nestjs/common';
import { ApiBadRequestResponse, ApiCreatedResponse, ApiOperation, ApiTags } from '@nestjs/swagger';
import { BookmarkService } from '../service/bookmark.service';
import { ApiGuard, GetUserId } from '../../../global/decorator';
import { CreateBookmarkListRequestDto } from '../../../global/dto';
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

  @Get()
  findAll() {
    return this.bookmarkService.findAll();
  }

  @Get(':id')
  findOne(@Param('id') id: string) {
    return this.bookmarkService.findOne(+id);
  }

  @Patch(':id')
  update(@Param('id') id: string) {
    return this.bookmarkService.update(+id);
  }

  @Delete(':id')
  remove(@Param('id') id: string) {
    return this.bookmarkService.remove(+id);
  }
}
