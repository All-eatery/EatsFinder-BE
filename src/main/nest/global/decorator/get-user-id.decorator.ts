import { createParamDecorator, ExecutionContext } from '@nestjs/common';

export const GetUserId = createParamDecorator((_: undefined, context: ExecutionContext) => {
  const req = context.switchToHttp().getRequest();
  return req.user?.id;
});
