import { Body, Controller, Get, Param, Post } from '@nestjs/common';
import { PlaceService } from '../service/place.service';
import { Places } from '@prisma/client';
import {
  ApiBody,
  ApiConflictResponse,
  ApiCreatedResponse,
  ApiOkResponse,
  ApiOperation,
  ApiTags,
} from '@nestjs/swagger';
import { CreatePlaceRequestDto, CreatePlaceResponseDto, FindPlaceResponseDto } from '../../../global/dto';

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

  @Get(':name')
  @ApiOperation({ summary: '등록된 맛집 조회' })
  @ApiOkResponse({ type: [FindPlaceResponseDto] })
  async findPlace(@Param('name') name: string) {
    return await this.placeService.findPlace(name);
  }
}
