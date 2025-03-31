package com.eatsfinder.domain.follow.dto

import com.eatsfinder.global.pagination.PaginationCursorItemsResponse


data class PaginationFollowerResponse(
    val pagination: PaginationCursorItemsResponse,
    val followList: List<FollowerListResponse>
)
