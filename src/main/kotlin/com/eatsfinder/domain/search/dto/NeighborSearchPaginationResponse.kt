package com.eatsfinder.domain.search.dto

import com.eatsfinder.global.pagination.PaginationItemsResponse

data class NeighborSearchPaginationResponse(
    val pagination: PaginationItemsResponse? = null,
    val items: List<NeighborPostResponse>,
    val lastItemId: Long?
)