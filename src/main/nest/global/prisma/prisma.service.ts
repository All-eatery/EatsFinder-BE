import { INestApplication, Injectable, Logger, OnModuleDestroy, OnModuleInit } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { PrismaClient } from '@prisma/client';
import { getDMMF } from '@prisma/internals';
import { readFileSync } from 'fs';
import { join } from 'path';

@Injectable()
export class PrismaService extends PrismaClient implements OnModuleInit, OnModuleDestroy {
  private modelFields: { [model: string]: string[] } = {};

  constructor(private readonly configService: ConfigService) {
    super({
      datasources: {
        db: {
          url: configService.get('ENV_DATABASE_URL'),
        },
      },
    });

    this.setupMiddleware();
    this.loadModelFields();
  }

  private async loadModelFields() {
    try {
      const schemaPath = join(__dirname, '../../../src/main/nest/global/prisma/schema.prisma');
      const schema = readFileSync(schemaPath, 'utf-8');
      const dmmf = await getDMMF({ datamodel: schema });

      dmmf.datamodel.models.forEach((model) => {
        this.modelFields[model.name] = model.fields.map((field) => field.name);
      });
    } catch (error) {
      Logger.error('Error loading model fields:', error);
    }
  }

  private setupMiddleware() {
    this.$use(async (params, next) => {
      const now = new Date();
      const kstOffset = 9 * 60 * 60 * 1000;
      const kstTime = new Date(now.getTime() + kstOffset);

      const fields = this.modelFields[params.model];

      if (!fields) {
        console.warn(params.model);
        return next(params);
      }

      if (params.action === 'create') {
        if (fields.includes('createdAt') && !params.args.data.createdAt) {
          params.args.data.createdAt = kstTime;
          params.args.data.updatedAt = kstTime;
        }
      }

      if (params.action === 'update') {
        if (fields.includes('updatedAt')) {
          params.args.data.updatedAt = kstTime;
        }
      }
      return next(params);
    });
  }

  async onModuleInit() {
    try {
      await this.$connect();
    } catch (err) {
      Logger.error(err);
    }
  }

  async onModuleDestroy() {
    try {
      await this.$disconnect();
    } catch (err) {
      Logger.error(err);
    }
  }

  async enableShutdownHooks(app: INestApplication) {
    process.on('beforeExit', async () => {
      await app.close();
    });
  }
}
