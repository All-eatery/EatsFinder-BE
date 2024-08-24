import { applyDecorators, UseInterceptors } from '@nestjs/common';
import { AnyFilesInterceptor } from '@nestjs/platform-express';
import { ApiBody, ApiConsumes } from '@nestjs/swagger';
import { UpdatePostRequestDto } from '../dto';

export function ApiUpdatePost() {
  return applyDecorators(
    UseInterceptors(AnyFilesInterceptor()),
    ApiConsumes('multipart/form-data'),
    ApiBody({ type: UpdatePostRequestDto }),
  );
}
