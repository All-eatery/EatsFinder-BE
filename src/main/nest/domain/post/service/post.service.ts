import { Injectable } from '@nestjs/common';
import { S3Service } from '../../../global/s3/s3.service';

@Injectable()
export class PostService {
  constructor(private readonly s3Service: S3Service) {}

  async imageUpload(files: Express.Multer.File[]) {
    const uploadToimage = files.map(async (file) => {
      //Todo key는 임시로 [시간_파일명] 으로 진행함
      const key = `places/${new Date().toISOString().replace(/:/g, '-')}_${file.originalname}`;
      const body = file.buffer;
      const contentType = file.mimetype;

      await this.s3Service.uploadS3(key, body, contentType);
      return key;
    });
    return await Promise.all(uploadToimage);
  }
}
