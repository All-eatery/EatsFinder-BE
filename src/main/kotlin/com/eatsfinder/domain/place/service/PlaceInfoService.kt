package com.eatsfinder.domain.place.service

import com.eatsfinder.domain.place.dto.PlaceInfoRequest
import com.eatsfinder.domain.place.dto.PlaceInfoResponse

interface PlaceInfoService {

    fun getPlaceMapInfo(req: PlaceInfoRequest): List<PlaceInfoResponse>
}

