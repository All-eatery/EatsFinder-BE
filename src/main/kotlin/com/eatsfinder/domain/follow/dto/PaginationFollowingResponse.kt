package com.eatsfinder.domain.follow.dto

import com.eatsfinder.global.pagination.PaginationCursorItemsResponse


data class PaginationFollowingResponse(
    val pagination: PaginationCursorItemsResponse,
    val followList: List<FollowingListResponse>
)

