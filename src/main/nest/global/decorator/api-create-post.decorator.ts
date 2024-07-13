import { applyDecorators, UseInterceptors } from '@nestjs/common';
import { AnyFilesInterceptor } from '@nestjs/platform-express';
import { ApiBody, ApiConsumes } from '@nestjs/swagger';
import { CreatePostRequestDto } from '../dto';

export function ApiCreatePost() {
  return applyDecorators(
    UseInterceptors(AnyFilesInterceptor()),
    ApiConsumes('multipart/form-data'),
    ApiBody({ type: CreatePostRequestDto }),
  );
}
