import { Body, Controller, Post } from '@nestjs/common';
import { MenuService } from '../service/menu.service';
import { PlaceMenus } from '@prisma/client';
import { ApiBody, ApiConflictResponse, ApiCreatedResponse, ApiOperation, ApiTags } from '@nestjs/swagger';
import { CreateMenuRequestDto, CreateMenuResponseDto } from '../../../../global/dto';

@ApiTags('Menu')
@Controller('menus')
export class MenuController {
  constructor(private readonly menuService: MenuService) {}

  @Post()
  @ApiOperation({ summary: '맛집에 메뉴 등록' })
  @ApiCreatedResponse({ type: CreateMenuResponseDto })
  @ApiConflictResponse({ description: '이미 메뉴가 등록되어 있습니다.' })
  @ApiBody({ type: CreateMenuRequestDto })
  async createPlace(@Body() dto: CreateMenuRequestDto) {
    const { id, menu, placeId }: PlaceMenus = await this.menuService.createMenu(dto);
    return { id: Number(id), menu: menu, placeId: Number(placeId) };
  }
}
