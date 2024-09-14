import { PickType } from '@nestjs/swagger';
import { CreateUserRequestDto } from './create-user-request.dto';

export class FindPasswordRequestDto extends PickType(CreateUserRequestDto, ['name', 'email'] as const) {}
