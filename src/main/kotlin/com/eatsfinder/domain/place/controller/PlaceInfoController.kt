package com.eatsfinder.domain.place.controller

import com.eatsfinder.domain.place.dto.PlaceInfoRequest
import com.eatsfinder.domain.place.dto.PlaceInfoResponse
import com.eatsfinder.domain.place.service.PlaceInfoService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PlaceInfoController(
    private val placeInfoService: PlaceInfoService
) {

    @Operation(summary = "맛집 정보 지도 조회")
    @GetMapping("/place/map")
    fun getPlaceInfoMap(
        req: PlaceInfoRequest
    ): ResponseEntity<List<PlaceInfoResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(placeInfoService.getPlaceMapInfo(req))
    }
}