import { ApiProperty } from '@nestjs/swagger';

export class LoginUserRequestDto {
  @ApiProperty()
  email: string;

  @ApiProperty()
  password: string;
}
