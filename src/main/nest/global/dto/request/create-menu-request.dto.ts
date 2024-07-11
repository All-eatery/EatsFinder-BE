import { ApiProperty } from '@nestjs/swagger';

export class CreateMenuRequestDto {
  @ApiProperty()
  menu: string;

  @ApiProperty()
  placeId: number;
}
