package com.eatsfinder.global.pagination

data class PaginationItemsResponse(
    val totalItems: Long,
    val itemsPerPage: Int,
    val totalPage: Long,
    val currentPage: Int,
    val isLastPage: Boolean
)
