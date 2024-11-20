import { ApiProperty } from '@nestjs/swagger';
import { IsNotEmpty, IsNumberString, IsString } from 'class-validator';

export class RemoveBookmarkPlaceDto {
  @ApiProperty({ example: '1,2,5 또는 all', description: '삭제할 placeId, 쉼표로 구분된 숫자 또는 all' })
  @IsString()
  @IsNotEmpty()
  placeId: string;

  @ApiProperty({ example: 1, description: '삭제할 리스트' })
  @IsNumberString()
  @IsNotEmpty()
  listId: string;
}
