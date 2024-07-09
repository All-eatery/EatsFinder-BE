import { Controller, Post, UploadedFile, UploadedFiles, UseInterceptors } from '@nestjs/common';
import { ApiBody, ApiConsumes, ApiTags } from '@nestjs/swagger';
import { PostService } from '../service/post.service';
import { FileInterceptor, FilesInterceptor } from '@nestjs/platform-express';

@ApiTags('Post')
@Controller('posts')
export class PostController {
  constructor(private readonly postService: PostService) {}

  //Todo 임시 이미지 업로드 API 기능 구현
  @Post('image')
  @UseInterceptors(FilesInterceptor('files', 5))
  @ApiConsumes('multipart/form-data')
  @ApiBody({
    description: 'File upload',
    schema: {
      type: 'object',
      properties: {
        files: {
          type: 'array',
          items: {
            type: 'string',
            format: 'binary',
          },
        },
      },
    },
  })
  async saveImage(@UploadedFiles() files: Array<Express.Multer.File>) {
    return await this.postService.imageUpload(files);
  }
}
