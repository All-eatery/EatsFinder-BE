package com.eatsfinder.domain.search.dto

import com.eatsfinder.global.pagination.PaginationItemsResponse

data class PlaceSearchPaginationResponse(
    val pagination: PaginationItemsResponse? = null,
    val items: List<PlaceSearchResponse>,
    val lastItemId: Long?
)