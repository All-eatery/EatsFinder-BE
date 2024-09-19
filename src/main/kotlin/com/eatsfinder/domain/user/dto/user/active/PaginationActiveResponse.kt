package com.eatsfinder.domain.user.dto.user.active

data class PaginationActiveResponse(
    val totalActive: Long,
    val activesPerPage: Int,
    val totalPage: Long,
    val currentPage: Int,
    val isLastPage: Boolean
)
