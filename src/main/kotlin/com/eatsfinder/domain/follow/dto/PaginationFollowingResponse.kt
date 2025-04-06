package com.eatsfinder.domain.follow.dto

import com.eatsfinder.global.pagination.PaginationItemsResponse


data class PaginationFollowingResponse(
    val pagination: PaginationItemsResponse,
    val items: List<FollowerListResponse>,
    val lastItemId: Long?
)

