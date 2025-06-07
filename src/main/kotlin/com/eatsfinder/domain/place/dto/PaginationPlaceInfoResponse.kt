package com.eatsfinder.domain.place.dto

import com.eatsfinder.global.pagination.PaginationItemsResponse

data class PaginationPlaceInfoResponse(
    val pagination: PaginationItemsResponse? = null,
    val items: List<PlaceInfoResponse>,
    val lastItemId: Long? = null,
)
