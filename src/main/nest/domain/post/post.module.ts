import { Module } from '@nestjs/common';
import { PostService } from './service/post.service';
import { PostController } from './controller/post.controller';
import { S3Module } from '../../global/s3/s3.module';

@Module({
  imports: [S3Module],
  controllers: [PostController],
  providers: [PostService],
})
export class PostModule {}
