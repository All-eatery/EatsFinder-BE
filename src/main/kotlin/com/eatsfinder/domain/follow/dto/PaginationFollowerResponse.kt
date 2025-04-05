package com.eatsfinder.domain.follow.dto

import com.eatsfinder.global.pagination.PaginationItemsResponse


data class PaginationFollowerResponse(
    val pagination: PaginationItemsResponse,
    val items: List<FollowingListResponse>,
    val lastItemId: Long?
)
