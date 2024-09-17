import { Controller, Get } from '@nestjs/common';
import { AppService } from './app.service';
import { ApiOkResponse, ApiOperation } from '@nestjs/swagger';

@Controller()
export class AppController {
  constructor(private readonly appService: AppService) {}

  @Get()
  @ApiOperation({ summary: '서버 상태 체크' })
  @ApiOkResponse({ description: 'health check' })
  health(): string {
    return this.appService.health();
  }
}
