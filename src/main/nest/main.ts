import { NestFactory } from '@nestjs/core';
import { ConfigService } from '@nestjs/config';
import { ValidationPipe } from '@nestjs/common';
import { AppModule } from './app/app.module';
import { PrismaService } from './global/prisma/prisma.service';
import { setupSwagger } from './global/swagger/swagger.config';
import { BigIntInterceptor } from './global/bigint-to-string.interceptor';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  const configService = app.get(ConfigService);

  app.enableCors({
    origin: true,
    methods: ['GET', 'HEAD', 'PUT', 'PATCH', 'POST', 'DELETE', 'OPTIONS'],
    credentials: true,
  });

  app.useGlobalInterceptors(new BigIntInterceptor());
  app.useGlobalPipes(new ValidationPipe({ transform: true }));

  const prismaService = app.get(PrismaService);
  await prismaService.enableShutdownHooks(app);

  setupSwagger(app);

  const PORT = configService.get<number>('ENV_NESTJS_PORT');
  await app.listen(PORT);
}
bootstrap();
