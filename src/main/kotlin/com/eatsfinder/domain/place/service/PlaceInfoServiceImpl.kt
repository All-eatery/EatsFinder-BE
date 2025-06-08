package com.eatsfinder.domain.place.service

import com.eatsfinder.domain.place.dto.PlaceInfoRequest
import com.eatsfinder.domain.place.dto.PlaceInfoResponse
import com.eatsfinder.domain.place.repository.PlaceRepository
import org.springframework.stereotype.Service

@Service
class PlaceInfoServiceImpl(
    private val placeRepository: PlaceRepository
): PlaceInfoService {
    override fun getPlaceMapInfo(req: PlaceInfoRequest): List<PlaceInfoResponse> {
        val map = placeRepository.findByXBetweenAndYBetween(req.oa, req.ha, req.qa, req.pa)
        return PlaceInfoResponse.from(map)
    }
}