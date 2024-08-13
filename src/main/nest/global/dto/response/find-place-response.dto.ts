import { ApiProperty } from '@nestjs/swagger';

export class FindPlaceResponseDto {
  @ApiProperty()
  id: number;

  @ApiProperty()
  name: string;

  @ApiProperty()
  roadAddress: string;

  @ApiProperty()
  thumbnailUrl: string;
}
