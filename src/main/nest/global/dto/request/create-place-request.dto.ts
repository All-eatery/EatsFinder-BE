import { ApiProperty } from '@nestjs/swagger';

export class CreatePlaceRequestDto {
  @ApiProperty()
  name: string;

  @ApiProperty()
  address: string;

  @ApiProperty()
  roadAddress?: string;

  @ApiProperty()
  telephone?: string;

  @ApiProperty()
  x: number;

  @ApiProperty()
  y: number;

  @ApiProperty()
  category: string;

  @ApiProperty()
  categoryName: string;

  @ApiProperty()
  categoryCode: string;
}
