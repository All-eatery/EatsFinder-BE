import { Module } from '@nestjs/common';
import { PlaceService } from './service/place.service';
import { PlaceController } from './controller/place.controller';
import { MenuModule } from './menu/menu.module';

@Module({
  controllers: [PlaceController],
  providers: [PlaceService],
  imports: [MenuModule],
})
export class PlaceModule {}
