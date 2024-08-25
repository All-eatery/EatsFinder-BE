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
  Patch,
  Delete,
} from '@nestjs/common';
import {
  ApiBadRequestResponse,
  ApiCreatedResponse,
  ApiForbiddenResponse,
  ApiNotFoundResponse,
  ApiOkResponse,
  ApiOperation,
  ApiResponse,
  ApiTags,
  ApiUnauthorizedResponse,
} from '@nestjs/swagger';
import { PostService } from '../service/post.service';
import { CreatePostRequestDto, FindOnePostResponseDto, UpdatePostRequestDto } from '../../../global/dto';
import { GetUserId, ApiGuard, ApiCreatePost, ApiUpdatePost } from '../../../global/decorator';

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

  @Get(':id/check')
  @ApiOperation({ summary: '유저 게시물 수정 체크' })
  @ApiOkResponse({ description: '수정가능 합니다' })
  @ApiBadRequestResponse({ description: '해당 게시물은 존재하지 않습니다' })
  @ApiForbiddenResponse({ description: '게시물은 24시간 이내에 수정이 가능해요' })
  @ApiResponse({ status: 423, description: '다른 사용자의 반응(댓글)이 있는 글은 수정할 수 없어요' })
  async checkPost(@Param('id', ParseIntPipe) id: number) {
    return await this.postService.checkPost(id);
  }

  @Patch(':id')
  @ApiGuard()
  @ApiUpdatePost()
  @ApiOperation({ summary: '유저 게시물 수정' })
  @ApiOkResponse({ description: '수정되었습니다' })
  @ApiBadRequestResponse({ description: '최대 5개까지 업로드 가능합니다.' })
  @ApiUnauthorizedResponse({ description: '작성자가 아닙니다' })
  @ApiNotFoundResponse({ description: '대표 이미지는 필수입니다.' })
  async updatePost(
    @GetUserId() userId: number,
    @Param('id', ParseIntPipe) id: number,
    @UploadedFiles() files: Array<Express.Multer.File>,
    @Body() dto: UpdatePostRequestDto,
  ) {
    return await this.postService.updatePost(userId, id, files, dto);
  }

  @Delete(':id')
  @ApiGuard()
  @ApiOperation({ summary: '유저 게시물 삭제' })
  @ApiOkResponse({ description: '삭제되었습니다' })
  @ApiUnauthorizedResponse({ description: '작성자가 아닙니다' })
  @ApiNotFoundResponse({ description: '해당 게시물은 존재하지 않습니다' })
  async deletePost(@GetUserId() userId: number, @Param('id', ParseIntPipe) id: number) {
    return await this.postService.deletePost(userId, id);
  }
}
