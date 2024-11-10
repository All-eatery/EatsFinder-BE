import { ApiProperty } from '@nestjs/swagger';

class Users {
  @ApiProperty({ example: 'example' })
  nickname: string;

  @ApiProperty({ example: 'https://example.com/example.jpg', nullable: true })
  profileImage: string | null;
}

class Items {
  @ApiProperty({ example: 1 })
  id: number;

  @ApiProperty({ example: 'https://example.com/example.jpg' })
  thumbnailUrl: string;

  @ApiProperty({ type: Users })
  users: Users;

  @ApiProperty({ example: false })
  likeStatus: boolean;
}

class Pagination {
  @ApiProperty({ example: 123 })
  totalItems: number;

  @ApiProperty({ example: 20 })
  itemsPerPage: number;
}

export class FindAllPostResponseDto {
  @ApiProperty({ type: Pagination })
  pagination: Pagination;

  @ApiProperty({ type: [Items] })
  items: Items[];

  @ApiProperty({ example: 321 })
  lastItemId: number;
}
