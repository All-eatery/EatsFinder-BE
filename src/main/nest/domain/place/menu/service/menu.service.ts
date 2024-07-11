import { ConflictException, Injectable } from '@nestjs/common';
import { PrismaService } from '../../../../global/prisma/prisma.service';
import { CreateMenuRequestDto } from '../../../../global/dto';

@Injectable()
export class MenuService {
  constructor(private readonly prismaService: PrismaService) {}

  async createMenu(dto: CreateMenuRequestDto) {
    const { menu, placeId } = dto;

    const menus = await this.prismaService.placeMenus.findMany({ where: { menu, placeId } });
    const isMenuData = menus.length === 0 ? false : menus;
    if (isMenuData) throw new ConflictException('이미 메뉴가 등록되어 있습니다.');

    return await this.prismaService.placeMenus.create({
      data: {
        menu,
        placeId,
      },
    });
  }
}
