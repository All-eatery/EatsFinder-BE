import { applyDecorators, UseGuards } from '@nestjs/common';
import { ApiBearerAuth } from '@nestjs/swagger';
import { JwtAuthOptionGuard } from '../../domain/auth/guard/jwt-auth-optional.guard';

export function ApiOptionGuard() {
  return applyDecorators(UseGuards(JwtAuthOptionGuard), ApiBearerAuth('accessToken'));
}
