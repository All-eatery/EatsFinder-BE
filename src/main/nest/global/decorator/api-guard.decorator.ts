import { applyDecorators, UseGuards } from '@nestjs/common';
import { ApiBearerAuth } from '@nestjs/swagger';
import { JwtAuthGuard } from '../../domain/auth/guard/jwt-auth.guard';

export function ApiGuard() {
  return applyDecorators(UseGuards(JwtAuthGuard), ApiBearerAuth('accessToken'));
}
