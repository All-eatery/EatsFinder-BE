import { applyDecorators, UseInterceptors } from '@nestjs/common';
import { FilesInterceptor } from '@nestjs/platform-express';
import { ApiBody, ApiConsumes } from '@nestjs/swagger';

export function ApiImageFiles(fieldName: string) {
  return applyDecorators(
    UseInterceptors(FilesInterceptor(fieldName)),
    ApiConsumes('multipart/form-data'),
    ApiBody({
      description: '이미지 업로드',
      schema: {
        type: 'object',
        properties: {
          fieldName: {
            type: 'array',
            items: {
              type: 'string',
              format: 'binary',
            },
          },
        },
      },
    }),
  );
}
