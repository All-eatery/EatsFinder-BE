import { Controller, Post, UploadedFile, UploadedFiles, UseInterceptors } from '@nestjs/common';
import { ApiBody, ApiConsumes, ApiOperation, ApiTags } from '@nestjs/swagger';
import { PostService } from '../service/post.service';
import { FileInterceptor, FilesInterceptor } from '@nestjs/platform-express';

@ApiTags('Post')
@Controller('posts')
export class PostController {
  constructor(private readonly postService: PostService) {}

  //Todo 임시 이미지 업로드 API 기능 구현
  @Post('image')
  @ApiOperation({ summary: '이미지 업로드(임시 구현)' })
  @UseInterceptors(FilesInterceptor('files', 5))
  @ApiConsumes('multipart/form-data')
  @ApiBody({
    description: '이미지 업로드',
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
