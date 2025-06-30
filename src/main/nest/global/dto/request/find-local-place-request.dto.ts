import { ApiProperty } from '@nestjs/swagger';
import { Type } from 'class-transformer';
import { IsNumber } from 'class-validator';

export class FindLocalPlaceDto {
  /*
  동쪽 (East): 경도 oa
  서쪽 (West): 경도 ha
  남쪽 (South): 위도 qa
  북쪽 (North): 위도 pa
  */

  @ApiProperty({ example: 128.6978977, description: '동쪽(East) 경도' })
  @IsNumber()
  @Type(() => Number)
  oa: number;

  @ApiProperty({ example: 128.4466364, description: '서쪽(West) 경도' })
  @IsNumber()
  @Type(() => Number)
  ha: number;

  @ApiProperty({ example: 38.1515074, description: '남쪽(South) 위도' })
  @IsNumber()
  @Type(() => Number)
  qa: number;

  @ApiProperty({ example: 38.21960547, description: '북쪽(North) 위도' })
  @IsNumber()
  @Type(() => Number)
  pa: number;
}
