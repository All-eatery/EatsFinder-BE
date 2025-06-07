package com.eatsfinder.domain.place.controller

import com.eatsfinder.domain.place.dto.PaginationPlaceInfoResponse
import com.eatsfinder.domain.place.dto.PlaceInfoRequest
import com.eatsfinder.domain.place.service.PlaceInfoService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class PlaceInfoController(
    private val placeInfoService: PlaceInfoService
) {

    @Operation(summary = "맛집 정보 지도 조회")
    @GetMapping("/place/map")
    fun getNewPostByNeighbor(
        req: PlaceInfoRequest,
        @RequestParam cursorId: Long?,
        @RequestParam(defaultValue = "10") pageSize: Int
    ): ResponseEntity<PaginationPlaceInfoResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(placeInfoService.getPlaceMapInfo(req, cursorId, pageSize))
    }
}