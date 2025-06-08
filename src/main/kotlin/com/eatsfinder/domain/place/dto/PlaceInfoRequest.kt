package com.eatsfinder.domain.place.dto

data class PlaceInfoRequest (
    val oa: Double,
    val ha: Double,
    val qa: Double,
    val pa: Double
)

/*
동쪽 (East): 경도 oa
서쪽 (West): 경도 ha
남쪽 (South): 위도 qa
북쪽 (North): 위도 pa
*/