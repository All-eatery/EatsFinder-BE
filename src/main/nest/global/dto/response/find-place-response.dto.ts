import { ApiProperty } from '@nestjs/swagger';

export class FindPlaceResponseDto {
  @ApiProperty()
  id: number;

  @ApiProperty()
  name: string;

  @ApiProperty()
  address: string;

  @ApiProperty()
  thumbnailUrl: string;
}
