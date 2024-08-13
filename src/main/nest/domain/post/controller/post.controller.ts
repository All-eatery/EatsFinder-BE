import {
  Controller,
  BadRequestException,
  NotFoundException,
  UploadedFiles,
  ParseIntPipe,
  Body,
  Param,
  Post,
  Get,
} from '@nestjs/common';
import { ApiCreatedResponse, ApiNotFoundResponse, ApiOkResponse, ApiOperation, ApiTags } from '@nestjs/swagger';
import { PostService } from '../service/post.service';
import { CreatePostRequestDto, FindOnePostResponseDto } from '../../../global/dto';
import { GetUserId, ApiGuard, ApiCreatePost } from '../../../global/decorator';

@ApiTags('Post')
@Controller('posts')
export class PostController {
  constructor(private readonly postService: PostService) {}

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

  @Get(':id/details')
  @ApiOperation({ summary: '유저 게시물 단건 조회' })
  @ApiOkResponse({ type: FindOnePostResponseDto })
  @ApiNotFoundResponse({ description: '해당 게시물은 존재하지 않습니다.' })
  async findOnePost(@Param('id', ParseIntPipe) id: number) {
    return await this.postService.findOnePost(id);
  }
}
