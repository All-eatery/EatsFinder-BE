import { ApiProperty } from '@nestjs/swagger';

class BookmarkPlacePlaces {
  @ApiProperty({ example: 1 })
  id: number;

  @ApiProperty()
  name: string;

  @ApiProperty()
  roadAddress: string;

  @ApiProperty()
  depth2: string;

  @ApiProperty({ example: 'https://example.com/example.jpg' })
  thumbnailUrl: string;
}

class BookmarkPlaceItems {
  @ApiProperty({ example: 1 })
  id: number;

  @ApiProperty({ type: BookmarkPlacePlaces })
  places: BookmarkPlacePlaces;
}

class Pagination {
  @ApiProperty({ example: 123 })
  totalItems: number;

  @ApiProperty({ example: 10 })
  itemsPerPage: number;
}

export class FindAllBookmarkPlaceResponseDto {
  @ApiProperty({ type: Pagination })
  pagination: Pagination;

  @ApiProperty({ type: [BookmarkPlaceItems] })
  items: BookmarkPlaceItems[];

  @ApiProperty({ example: 321 })
  lastItemId: number;
}
