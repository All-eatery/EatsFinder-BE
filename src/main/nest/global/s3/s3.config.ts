import { S3Client } from '@aws-sdk/client-s3';
import { Injectable } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';

@Injectable()
export class S3Config {
  public s3Client: S3Client;

  constructor(private configService: ConfigService) {
    this.s3Client = new S3Client({
      region: this.configService.get<string>('ENV_AWS_S3_REGION'),
      credentials: {
        accessKeyId: this.configService.get<string>('ENV_AWS_S3_ACCESS_KEY_ID'),
        secretAccessKey: this.configService.get<string>('ENV_AWS_S3_SECRET_ACCESS_KEY'),
      },
    });
  }
}
