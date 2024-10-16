import { Body, Controller, Get, Param, ParseIntPipe, Post } from '@nestjs/common';
import { MenuService } from '../service/menu.service';
import { PlaceMenus } from '@prisma/client';
import {
  ApiBody,
  ApiConflictResponse,
  ApiCreatedResponse,
  ApiOkResponse,
  ApiOperation,
  ApiTags,
} from '@nestjs/swagger';
import { CreateMenuRequestDto, CreateMenuResponseDto, FindMenuResponseDto } from '../../../global/dto';

@ApiTags('Menu')
@Controller('menus')
export class MenuController {
  constructor(private readonly menuService: MenuService) {}

  @Post()
  @ApiOperation({ summary: '맛집에 메뉴 등록' })
  @ApiCreatedResponse({ type: CreateMenuResponseDto })
  @ApiConflictResponse({ description: '이미 메뉴가 등록되어 있습니다.' })
  @ApiBody({ type: CreateMenuRequestDto })
  async createMenu(@Body() dto: CreateMenuRequestDto) {
    const { id, menu, placeId }: PlaceMenus = await this.menuService.createMenu(dto);
    return { id: Number(id), menu: menu, placeId: Number(placeId) };
  }

  @Get(':id')
  @ApiOperation({ summary: '맛집의 메뉴 조회' })
  @ApiOkResponse({ type: [FindMenuResponseDto] })
  async findMenu(@Param('id', ParseIntPipe) placeId: number) {
    return await this.menuService.findMenu(placeId);
  }
}
