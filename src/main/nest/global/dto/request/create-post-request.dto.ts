import { ApiProperty } from '@nestjs/swagger';
import { Type } from 'class-transformer';
import { IsNumber } from 'class-validator';

export class CreatePostRequestDto {
  @ApiProperty()
  content?: string;

  @ApiProperty()
  menuTag: string;

  @ApiProperty()
  starRating: string;

  @ApiProperty({ type: Number })
  @IsNumber()
  @Type(() => Number)
  placeId: number;

  @ApiProperty()
  keywordId: string;

  @ApiProperty({ type: 'string', format: 'binary' })
  mainImg: any;

  @ApiProperty({ type: 'array', items: { type: 'string', format: 'binary' } })
  imgs: any;
}
