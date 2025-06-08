package com.eatsfinder.domain.place.dto

import com.eatsfinder.domain.place.model.Place

data class PlaceInfoResponse(
    val id: Long?,
    val name: String,
    val lng: Double,
    val lat: Double
) {
    companion object {
        fun from(places: List<Place>): List<PlaceInfoResponse> {
            val res = places.map { place ->
                PlaceInfoResponse(
                    id = place.id,
                    name = place.name,
                    lng = place.x,
                    lat = place.y
                )
            }
            return res
        }
    }
}
