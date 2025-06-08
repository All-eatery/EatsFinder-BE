import { ApiProperty } from '@nestjs/swagger';
import { Type } from 'class-transformer';
import { IsNumber, IsOptional, IsString } from 'class-validator';

export class FindAllBookmarkPlaceDto {
  @ApiProperty({ required: false })
  @IsOptional()
  @IsNumber()
  @Type(() => Number)
  cursor?: number;

  @ApiProperty({ required: false })
  @IsOptional()
  @IsString()
  @Type(() => String)
  keyword?: string;
}
