package com.eatsfinder.domain.like.dto

import com.eatsfinder.global.pagination.PaginationCursorItemsResponse

data class PaginationPostLikeResponse(
    val pagination: PaginationCursorItemsResponse,
    val postLikeList: List<PostLikeResponse>
)
