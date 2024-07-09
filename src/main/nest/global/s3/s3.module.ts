import { Module } from '@nestjs/common';
import { S3Service } from './s3.service';
import { ConfigModule } from '@nestjs/config';
import { S3Config } from './s3.config';

@Module({
  imports: [ConfigModule],
  providers: [S3Config, S3Service],
  exports: [S3Service],
})
export class S3Module {}
