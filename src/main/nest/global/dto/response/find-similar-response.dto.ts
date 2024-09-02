import { ApiProperty } from '@nestjs/swagger';

export class FindSimilarResponseDto {
  @ApiProperty()
  id: number;

  @ApiProperty()
  nickname: string;

  @ApiProperty()
  profileImage: string;

  @ApiProperty()
  followerCount: number;

  @ApiProperty()
  postCount: number;

  @ApiProperty()
  followerStatus: boolean;
}
