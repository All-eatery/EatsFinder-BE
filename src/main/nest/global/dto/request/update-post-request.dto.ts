import { OmitType, PartialType } from '@nestjs/swagger';
import { CreatePostRequestDto } from './create-post-request.dto';

export class UpdatePostRequestDto extends PartialType(OmitType(CreatePostRequestDto, ['placeId'] as const)) {}
