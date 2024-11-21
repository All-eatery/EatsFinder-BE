import { ApiProperty } from '@nestjs/swagger';

export class UpdateBookmarkListRequestDto {
  @ApiProperty()
  title: string;
}
