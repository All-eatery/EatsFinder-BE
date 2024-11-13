import { Module } from '@nestjs/common';
import { BookmarkService } from './service/bookmark.service';
import { BookmarkController } from './controller/bookmark.controller';

@Module({
  controllers: [BookmarkController],
  providers: [BookmarkService],
})
export class BookmarkModule {}
