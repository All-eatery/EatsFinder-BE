import { ApiProperty } from '@nestjs/swagger';
import { Type } from 'class-transformer';
import { IsArray } from 'class-validator';

export class UpdateBookmarkPlaceRequestDto {
  @ApiProperty({ example: [1, 2], type: [Number] })
  @IsArray()
  @Type(() => Number)
  places: number[];

  @ApiProperty({ example: [1, 2], type: [Number] })
  @IsArray()
  @Type(() => Number)
  lists: number[];
}
