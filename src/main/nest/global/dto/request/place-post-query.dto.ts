import { ApiPropertyOptional } from '@nestjs/swagger';
import { IsOptional, IsIn, IsInt } from 'class-validator';
import { Type } from 'class-transformer';

export class PlacePostQueryDto {
  @ApiPropertyOptional({
    description: '정렬 방식 (recent: 최신순, like: 좋아요순)',
    enum: ['recent', 'like'],
    default: 'recent',
  })
  @IsOptional()
  @IsIn(['recent', 'like'])
  sort?: 'recent' | 'like' = 'recent';

  @ApiPropertyOptional({
    description: '마지막 게시물 ID',
    type: Number,
    example: 123,
  })
  @IsOptional()
  @Type(() => Number)
  @IsInt()
  cursor?: number;
}
