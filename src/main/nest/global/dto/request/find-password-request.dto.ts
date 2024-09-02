import { PickType } from '@nestjs/swagger';
import { CreateUserDto } from '../../../domain/auth/dto/create-user.dto';

export class FindPasswordRequestDto extends PickType(CreateUserDto, ['name', 'email'] as const) {}
