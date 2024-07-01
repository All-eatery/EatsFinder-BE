import { NestFactory } from '@nestjs/core';
import { ConfigService } from '@nestjs/config';
import { AppModule } from './app/app.module';
import { PrismaService } from './global/prisma/prisma.service';
import { setupSwagger } from './global/swagger/swagger.config';
import { BigIntInterceptor } from './global/bigint-to-string.interceptor';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  const configService = app.get(ConfigService);

  app.useGlobalInterceptors(new BigIntInterceptor());

  const prismaService = app.get(PrismaService);
  await prismaService.enableShutdownHooks(app);

  setupSwagger(app);

  const PORT = configService.get<number>('ENV_NESTJS_PORT');
  await app.listen(PORT);
}
bootstrap();
