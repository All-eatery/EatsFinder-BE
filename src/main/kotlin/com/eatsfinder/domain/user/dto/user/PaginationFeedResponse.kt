package com.eatsfinder.domain.user.dto.user

data class PaginationFeedResponse(
    val totalFeed: Long,
    val feedsPerPage: Int,
    val totalPage: Long,
    val currentPage: Int,
    val isLastPage: Boolean
)
