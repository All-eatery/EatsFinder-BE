import { Module } from '@nestjs/common';
import { PlaceService } from './service/place.service';
import { PlaceController } from './controller/place.controller';

@Module({
  controllers: [PlaceController],
  providers: [PlaceService],
})
export class PlaceModule {}
