package com.eatsfinder.domain.like.dto

import com.eatsfinder.global.pagination.PaginationItemsResponse

data class PaginationPostLikeResponse(
    val pagination: PaginationItemsResponse,
    val items: List<PostLikeResponse>,
    val lastItemId: Long?
)
