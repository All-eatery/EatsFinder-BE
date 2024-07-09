import { Controller, Post, UploadedFiles } from '@nestjs/common';
import { ApiOperation, ApiTags } from '@nestjs/swagger';
import { PostService } from '../service/post.service';
import { ApiImageFiles } from '../../../global/decorator/files.decorator';

@ApiTags('Post')
@Controller('posts')
export class PostController {
  constructor(private readonly postService: PostService) {}

  //Todo 임시 이미지 업로드 API 기능 구현
  @Post('image')
  @ApiOperation({ summary: '이미지 업로드(임시 구현)' })
  @ApiImageFiles('files')
  async saveImage(@UploadedFiles() files: Array<Express.Multer.File>) {
    return await this.postService.imageUpload(files);
  }
}
