import { ApiProperty } from '@nestjs/swagger';

class Users {
  @ApiProperty({ example: 1 })
  id: number;

  @ApiProperty({ example: 'example' })
  nickname: string;

  @ApiProperty({ example: 'https://example.com/example.jpg' })
  profileImage: string | null;
}

class Categories {
  @ApiProperty({ example: '한식' })
  name: string;
}

class Places {
  @ApiProperty({ example: 1 })
  id: number;

  @ApiProperty({ example: '속초항아리물회' })
  name: string;

  @ApiProperty({ example: '강원특별자치도 속초시 조양동 1442-11' })
  address: string;

  @ApiProperty({ example: '강원특별자치도 속초시 해오름로188번길 11' })
  roadAddress: string;

  @ApiProperty({ example: 128.601247028514 })
  x: number;

  @ApiProperty({ example: 38.19155114124001 })
  y: number;

  @ApiProperty({ example: 38.19155114124001 })
  categories: Categories;
}

export class FindOnePostResponseDto {
  @ApiProperty({ example: 1 })
  id: number;

  @ApiProperty({ example: '맛있어요' })
  content: string;

  @ApiProperty({ example: 'https://example.com/example.jpg' })
  thumbnailUrl: string;

  @ApiProperty({ example: 'https://example.com/example.jpg' })
  imageUrl: string | null;

  @ApiProperty({ example: ['항아리모듬물회', '오징어회'] })
  menuTag: string[];

  @ApiProperty({ example: '2,8,15' })
  keywordTag: string;

  @ApiProperty({ example: 0 })
  likeCount: number;

  @ApiProperty({ example: '2024-07-14T04:07:01.992Z' })
  createdAt: Date;

  @ApiProperty()
  users: Users;

  @ApiProperty()
  places: Places;

  @ApiProperty({ example: 4.5 })
  starRatings: number;
}
