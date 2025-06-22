import { ApiProperty } from '@nestjs/swagger';

export class PlaceDetailResponseDto {
  @ApiProperty({ example: 1 })
  id: number;

  @ApiProperty({ example: '맛집 이름' })
  name: string;

  @ApiProperty({ example: '맛집 지번 주소' })
  address: string;

  @ApiProperty({ example: '맛집 도로명 주소' })
  roadAddress: string;

  @ApiProperty({ example: 128.601247028514 })
  x: number;

  @ApiProperty({ example: 38.19155114124001 })
  y: number;

  @ApiProperty({
    example: 'https://example.com/example.jpg',
  })
  thumbnailUrl: string;

  @ApiProperty({ example: 'FR01, AS01, SM01, AS06', nullable: true })
  keywordTag: string | null;

  @ApiProperty({
    example: ['메뉴1', '메뉴2'],
    type: [String],
  })
  menuNames: string[];
}
