package com.eatsfinder.domain.place.dto

import com.eatsfinder.domain.place.model.Place

data class PlaceInfoResponse(
    val id: Long?,
    val name: String,
    val x: Double,
    val y: Double
) {
    companion object {
        fun from(place: Place): PlaceInfoResponse {
            return PlaceInfoResponse(
                id = place.id,
                name = place.name,
                x = place.x,
                y = place.y
            )
        }
    }
}
