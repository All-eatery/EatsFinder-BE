import { BadRequestException, Body, Controller, NotFoundException, Post, UploadedFiles } from '@nestjs/common';
import { ApiCreatedResponse, ApiOperation, ApiTags } from '@nestjs/swagger';
import { PostService } from '../service/post.service';
import { CreatePostRequestDto } from '../../../global/dto';
import { GetUserId, ApiGuard, ApiCreatePost } from '../../../global/decorator';

@ApiTags('Post')
@Controller('posts')
export class PostController {
  constructor(private readonly postService: PostService) {}

  //Todo 임시 기능 구현
  @Post()
  @ApiGuard()
  @ApiOperation({ summary: '유저 게시물 등록' })
  @ApiCreatedResponse({ description: '게시물이 등록되었습니다.' })
  @ApiCreatePost()
  async createPost(
    @GetUserId() userId: number,
    @UploadedFiles() files: Array<Express.Multer.File>,
    @Body() dto: CreatePostRequestDto,
  ) {
    const mainImage = files.find((file) => file.fieldname === 'mainImg');
    if (!mainImage) throw new NotFoundException('대표 이미지는 필수입니다.');
    if (files.length > 5) throw new BadRequestException('최대 5개까지 업로드 가능합니다.');
    return await this.postService.createPost(userId, files, dto);
  }
}
