import { ApiProperty } from '@nestjs/swagger';
import { Type } from 'class-transformer';
import { IsArray, IsNumber } from 'class-validator';

export class CreateBookmarkPlaceDto {
  @ApiProperty({ example: 123 })
  @IsNumber()
  @Type(() => Number)
  place: number;

  @ApiProperty({ example: [1, 2], type: [Number] })
  @IsArray()
  @Type(() => Number)
  lists: number[];
}
