import { Body, Controller, DefaultValuePipe, Get, Param, ParseIntPipe, Post, Query } from '@nestjs/common';
import { PlaceService } from '../service/place.service';
import { Places } from '@prisma/client';
import {
  ApiBody,
  ApiConflictResponse,
  ApiCreatedResponse,
  ApiNotFoundResponse,
  ApiOkResponse,
  ApiOperation,
  ApiQuery,
  ApiTags,
} from '@nestjs/swagger';
import {
  CreatePlaceRequestDto,
  CreatePlaceResponseDto,
  FindLocalPlaceDto,
  FindLocalPlaceResponseDto,
  FindPlaceResponseDto,
  PlaceDetailResponseDto,
  PlacePostQueryDto,
} from '../../../global/dto';
import { ApiOptionGuard, GetUserId } from '../../../global/decorator';

@ApiTags('Place')
@Controller('places')
export class PlaceController {
  constructor(private readonly placeService: PlaceService) {}

  @Post()
  @ApiOperation({ summary: '새로운 맛집 등록' })
  @ApiCreatedResponse({ description: '맛집이 등록되었습니다.', type: CreatePlaceResponseDto })
  @ApiConflictResponse({ description: '입력하신 장소가 이미 존재합니다.' })
  @ApiBody({ type: CreatePlaceRequestDto })
  async createPlace(@Body() dto: CreatePlaceRequestDto) {
    const place: Places = await this.placeService.createPlace(dto);
    return { message: '맛집이 등록되었습니다.', id: Number(place.id) };
  }

  @Get(':name/name')
  @ApiOperation({ summary: '등록된 맛집 조회' })
  @ApiOkResponse({ type: [FindPlaceResponseDto] })
  async findPlace(@Param('name') name: string) {
    return await this.placeService.findPlace(name);
  }

  @Get('local')
  @ApiOperation({ summary: '주변 맛집 조회' })
  @ApiOkResponse({ type: [FindLocalPlaceResponseDto] })
  async findLocalPlace(@Query() query: FindLocalPlaceDto) {
    return await this.placeService.findLocalPlace(query);
  }

  @Get(':id/details')
  @ApiOperation({ summary: '맛집 정보(상세)' })
  @ApiOkResponse({ type: PlaceDetailResponseDto })
  @ApiNotFoundResponse({ description: '해당 맛집 정보는 존재하지 않습니다.' })
  async placeDetail(@Param('id', ParseIntPipe) id: number) {
    return await this.placeService.placeDetail(id);
  }

  @Get(':id/posts')
  @ApiOptionGuard()
  @ApiOperation({ summary: '맛집 정보(게시물)' })
  @ApiQuery({ name: 'cursor', type: Number, required: false })
  @ApiQuery({ name: 'sort', enum: ['recent', 'like'], required: false })
  @ApiNotFoundResponse({ description: '해당 맛집의 게시물이 존재하지 않습니다.' })
  async placePosts(
    @Param('id', ParseIntPipe) id: number,
    @GetUserId() userId: number,
    @Query() query: PlacePostQueryDto,
  ) {
    return await this.placeService.placePosts(id, userId, query);
  }
}
