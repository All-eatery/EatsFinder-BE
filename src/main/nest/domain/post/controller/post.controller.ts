import { BadRequestException, Controller, NotFoundException, Post, UploadedFiles } from '@nestjs/common';
import { ApiOperation, ApiTags } from '@nestjs/swagger';
import { PostService } from '../service/post.service';
import { ApiImageFilesWithDto } from '../../../global/decorator/api-files.decorator';

@ApiTags('Post')
@Controller('posts')
export class PostController {
  constructor(private readonly postService: PostService) {}

  //Todo 임시 이미지 업로드 API 기능 구현
  @Post('image')
  @ApiOperation({ summary: '이미지 업로드(임시 구현)' })
  @ApiImageFilesWithDto()
  async saveImage(@UploadedFiles() files: Array<Express.Multer.File>) {
    if (files.length === 0) throw new NotFoundException('필수 이미지가 없습니다.');
    if (files.length > 5) throw new BadRequestException('최대 5개까지 업로드 가능합니다.');
    return await this.postService.imageUpload(files);
  }
}
