import { BadRequestException, Injectable } from '@nestjs/common';
import { CreateBookmarkListRequestDto } from '../../../global/dto';
import { PrismaService } from '../../../global/prisma/prisma.service';

@Injectable()
export class BookmarkService {
  constructor(private readonly prismaService: PrismaService) {}

  async create(userId: number, dto: CreateBookmarkListRequestDto) {
    const totalBookmark = await this.prismaService.bookmarks.count({ where: { userId } });
    if (totalBookmark > 99) throw new BadRequestException('생성할 수 있는 최대 리스트 개수는 100개까지입니다.');
    return await this.prismaService.bookmarks.create({ data: { title: dto.listname, userId } });
  }

  findAll() {
    return `This action returns all bookmark`;
  }

  findOne(id: number) {
    return `This action returns a #${id} bookmark`;
  }

  update(id: number) {
    return `This action updates a #${id} bookmark`;
  }

  remove(id: number) {
    return `This action removes a #${id} bookmark`;
  }
}
