import { ApiProperty } from '@nestjs/swagger';

class MenuDto {
  @ApiProperty()
  id: bigint;

  @ApiProperty()
  menu: string;
}

export class FindMenuResponseDto {
  @ApiProperty({ type: [MenuDto] })
  allMenus: MenuDto[];

  @ApiProperty({ type: [MenuDto] })
  recommendMenus: MenuDto[];
}
