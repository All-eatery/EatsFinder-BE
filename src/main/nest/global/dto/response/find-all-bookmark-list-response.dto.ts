import { ApiProperty } from '@nestjs/swagger';

class BookmarkPlaces {
  @ApiProperty({ example: 'https://example.com/example.jpg' })
  thumbnailUrl: string;
}

class BookmarkListItems {
  @ApiProperty({ example: 1 })
  id: number;

  @ApiProperty({ example: 'string' })
  title: string;

  @ApiProperty({ example: 123 })
  count: number;

  @ApiProperty({
    type: [BookmarkPlaces],
    nullable: true,
    example: [{ thumbnailUrl: 'https://example.com/example.jpg' }],
  })
  bookmarkPlaces: BookmarkPlaces[] | null;
}

class Pagination {
  @ApiProperty({ example: 123 })
  totalItems: number;

  @ApiProperty({ example: 10 })
  itemsPerPage: number;
}

export class FindAllBookmarkListResponseDto {
  @ApiProperty({ type: Pagination })
  pagination: Pagination;

  @ApiProperty({ type: [BookmarkListItems] })
  items: BookmarkListItems[];

  @ApiProperty({ example: 321 })
  lastItemId: number;
}
