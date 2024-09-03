package com.eatsfinder.domain.post.dto

data class PaginationNeighborPostResponse(
    val page: Int,
    val size: Int,
    val totalPost: Long,
    val isLastPage: Boolean
)
