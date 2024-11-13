import { Injectable } from '@nestjs/common';

@Injectable()
export class BookmarkService {
  create() {
    return 'This action adds a new bookmark';
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
