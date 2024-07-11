import { ApiProperty } from '@nestjs/swagger';

export class FindMenuResponseDto {
  @ApiProperty()
  id: bigint;

  @ApiProperty()
  menu: string;
}
