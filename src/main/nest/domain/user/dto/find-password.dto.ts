import { PickType } from '@nestjs/swagger';
import { CreateUserDto } from '../../auth/dto/create-user.dto';

export class FindPasswordDto extends PickType(CreateUserDto, ['name', 'email'] as const) {}
