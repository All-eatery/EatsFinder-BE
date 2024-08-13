import { Module } from '@nestjs/common';
import { PostService } from './service/post.service';
import { PostController } from './controller/post.controller';
import { S3Module } from '../../global/s3/s3.module';
import { PlaceService } from '../place/service/place.service';
import { PlaceModule } from '../place/place.module';

@Module({
  imports: [S3Module, PlaceModule],
  controllers: [PostController],
  providers: [PostService, PlaceService],
})
export class PostModule {}
