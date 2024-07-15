import { ApiProperty } from '@nestjs/swagger';

export class CreatePlaceResponseDto {
  @ApiProperty()
  message: string;

  @ApiProperty()
  id: number;
}
