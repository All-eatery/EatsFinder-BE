package com.eatsfinder.domain.search.dto

import com.eatsfinder.global.pagination.PaginationItemsResponse

data class PostSearchPaginationResponse(
    val pagination: PaginationItemsResponse? = null,
    val items: List<PostSearchResponse>,
    val lastItemId: Long?
)