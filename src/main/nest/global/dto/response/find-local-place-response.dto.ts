import { ApiProperty } from '@nestjs/swagger';

class Categories {
  @ApiProperty()
  name: string;
}

class Posts {
  @ApiProperty()
  thumbnailUrl: string;
}

export class FindLocalPlaceResponseDto {
  @ApiProperty()
  id: number;

  @ApiProperty()
  name: string;

  @ApiProperty()
  roadAddress: string;

  @ApiProperty()
  starRatings: number;

  @ApiProperty()
  categories: Categories;

  @ApiProperty({ type: [Posts] })
  posts: Posts[];

  @ApiProperty()
  bookmarkStatus: boolean;
}
