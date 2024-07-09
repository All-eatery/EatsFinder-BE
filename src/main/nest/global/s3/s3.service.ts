import { Injectable } from '@nestjs/common';
import { S3Config } from './s3.config';
import { PutObjectCommand, PutObjectCommandInput } from '@aws-sdk/client-s3';
import { ConfigService } from '@nestjs/config';

@Injectable()
export class S3Service {
  constructor(
    private readonly s3Config: S3Config,
    private readonly configService: ConfigService,
  ) {}

  async uploadS3(Key: string, Body: any, ContentType: string) {
    const input: PutObjectCommandInput = {
      Bucket: this.configService.get<string>('ENV_AWS_S3_BUCKET'),
      Key,
      Body,
      ContentType,
    };
    return await this.s3Config.s3Client.send(new PutObjectCommand(input));
  }
}
