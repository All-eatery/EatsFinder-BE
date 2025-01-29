import { Controller, Get, Post, Patch, Param, Delete, Body, Query, ParseIntPipe } from '@nestjs/common';
import {
  ApiBadRequestResponse,
  ApiConflictResponse,
  ApiCreatedResponse,
  ApiNotFoundResponse,
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
  CreateBookmarkPlaceDto,
  FindAllBookmarkListDto,
  FindAllBookmarkListResponseDto,
  FindAllBookmarkPlaceDto,
  FindAllBookmarkPlaceResponseDto,
  RemoveBookmarkPlaceDto,
  UpdateBookmarkListRequestDto,
  UpdateBookmarkPlaceRequestDto,
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

  @Get('lists/:placeId')
  @ApiGuard()
  @ApiOperation({ summary: '장소가 들어가 있는 리스트들 조회' })
  async findBookmarkByPlaces(@GetUserId() userId: number, @Param('placeId') placeId: number) {
    return await this.bookmarkService.findBookmarkByPlaces(userId, placeId);
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

  @Delete('lists/:id')
  @ApiGuard()
  @ApiOperation({ summary: '리스트 삭제' })
  @ApiOkResponse({ description: '삭제되었습니다.' })
  @ApiBadRequestResponse({ description: '해당 리스트는 존재하지 않습니다.' })
  @ApiUnauthorizedResponse({ description: '본인 리스트만 삭제할 수 있습니다.' })
  async remove(@Param('id', ParseIntPipe) id: number, @GetUserId() userId: number) {
    return await this.bookmarkService.remove(id, userId);
  }

  @Post('places')
  @ApiGuard()
  @ApiOperation({ summary: '맛집 추가' })
  @ApiCreatedResponse({ description: '맛집이 추가되었습니다.' })
  @ApiNotFoundResponse({ description: '리스트가 존재하지 않습니다.' })
  @ApiBadRequestResponse({ description: '리스트에 추가할 수 없습니다.' })
  @ApiConflictResponse({ description: '이미 추가된 맛집입니다.' })
  async createBookmark(@GetUserId() userId: number, @Body() dto: CreateBookmarkPlaceDto) {
    await this.bookmarkService.createBookmark(userId, dto);
    return { message: '맛집이 추가되었습니다.' };
  }

  @Get('places/:id')
  @ApiGuard()
  @ApiQuery({ name: 'cursor', required: false })
  @ApiOperation({ summary: '맛집 조회' })
  @ApiNotFoundResponse({ description: '리스트가 존재하지 않습니다.' })
  @ApiOkResponse({ type: FindAllBookmarkPlaceResponseDto })
  async findBookmark(
    @GetUserId() userId: number,
    @Param('id', ParseIntPipe) id: number,
    @Query() query: FindAllBookmarkPlaceDto,
  ) {
    return await this.bookmarkService.findBookmark(userId, id, query.cursor);
  }

  @Patch('places')
  @ApiGuard()
  @ApiOperation({ summary: '맛집 수정(이동)' })
  @ApiCreatedResponse({ description: '맛집이 수정되었습니다.' })
  @ApiNotFoundResponse({ description: '리스트가 존재하지 않습니다.' })
  @ApiBadRequestResponse({ description: '리스트에 이동할 수 없습니다.' })
  async updateBookmark(@GetUserId() userId: number, @Body() dto: UpdateBookmarkPlaceRequestDto) {
    return await this.bookmarkService.updateBookmark(userId, dto);
  }

  @Delete('places')
  @ApiGuard()
  @ApiOperation({ summary: '맛집 삭제' })
  @ApiOkResponse({ description: '맛집이 삭제되었습니다.' })
  @ApiNotFoundResponse({ description: '리스트가 존재하지 않습니다.' })
  @ApiBadRequestResponse({ description: '리스트에 이동할 수 없습니다.' })
  @ApiConflictResponse({ description: '요청 형식에 맞지 않습니다.' })
  async removeBookmark(@GetUserId() userId: number, @Query() query: RemoveBookmarkPlaceDto) {
    return await this.bookmarkService.removeBookmark(userId, query);
  }
}
