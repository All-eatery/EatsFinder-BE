import { ApiProperty } from '@nestjs/swagger';
import { Type } from 'class-transformer';
import { IsNumber } from 'class-validator';

export class CreatePostRequestDto {
  @ApiProperty({ type: 'string', format: 'binary' })
  mainImg: any;

  @ApiProperty({ type: 'array', items: { type: 'string', format: 'binary' }, required: false })
  imgs: any;

  @ApiProperty({ required: false })
  content?: string;

  @ApiProperty({ required: false })
  menuTag: string;

  @ApiProperty({ required: false })
  keywordTag: string;

  @ApiProperty({ type: Number })
  @IsNumber()
  @Type(() => Number)
  starRating: number;

  @ApiProperty({ type: Number })
  @IsNumber()
  @Type(() => Number)
  placeId: number;
}
