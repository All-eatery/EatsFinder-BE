import { ConflictException, Injectable } from '@nestjs/common';
import { PrismaService } from '../../../global/prisma/prisma.service';
import { CreateMenuRequestDto } from '../../../global/dto';

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

  async findMenu(placeId: number) {
    const findManyMenu = await this.prismaService.placeMenus.findMany({ where: { placeId } });

    const allMenus = findManyMenu.map((menu) => ({
      id: Number(menu.id),
      menu: menu.menu,
    }));

    const randomMenus = (menus: any[]) => {
      for (let i = menus.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [menus[i], menus[j]] = [menus[j], menus[i]];
      }
      return menus.slice(0, 10);
    };
    const recommendMenus = randomMenus([...allMenus]);

    return { allMenus, recommendMenus };
  }
}
