import { ApiProperty } from '@nestjs/swagger';

export class CreateMenuResponseDto {
  @ApiProperty()
  id: bigint;

  @ApiProperty()
  menu: string;

  @ApiProperty()
  placeId: bigint;
}
