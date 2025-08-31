import { ApiProperty } from '@nestjs/swagger';
import { Type } from 'class-transformer';
import { IsNumber, IsOptional } from 'class-validator';

export class FindAllPostsDto {
  @ApiProperty({ required: false })
  @IsOptional()
  @IsNumber()
  @Type(() => Number)
  cursor?: number;

  @ApiProperty({ description: '조회할 게시물 개수', required: false, example: 5, default: 5 })
  @IsOptional()
  @IsNumber()
  @Type(() => Number)
  size?: number;
}
