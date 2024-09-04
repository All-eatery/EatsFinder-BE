package com.eatsfinder.domain.post.dto

data class PaginationNeighborPostResponse(
    val totalPosts: Long,
    val postsPerPage: Int,
    val totalPage: Long,
    val currentPage: Int,
    val isLastPage: Boolean
)
