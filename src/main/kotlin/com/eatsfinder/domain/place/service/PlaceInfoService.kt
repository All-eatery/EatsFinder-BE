package com.eatsfinder.domain.place.service

import com.eatsfinder.domain.place.dto.PaginationPlaceInfoResponse
import com.eatsfinder.domain.place.dto.PlaceInfoRequest

interface PlaceInfoService {

    fun getPlaceMapInfo(req: PlaceInfoRequest, cursorId: Long?, pageSize: Int): PaginationPlaceInfoResponse
}

