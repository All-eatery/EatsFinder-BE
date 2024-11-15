import { ApiProperty } from '@nestjs/swagger';

export class CreateBookmarkListRequestDto {
  @ApiProperty()
  listname: string;
}
