import { Controller } from '@nestjs/common';
import { PlaceService } from '../service/place.service';

@Controller('place')
export class PlaceController {
  constructor(private readonly placeService: PlaceService) {}
}
