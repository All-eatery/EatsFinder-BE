package com.eatsfinder.domain.search.dto

data class NeighborPostResponse(
    val imageUrl: String,
    val nickname: String,
    val postCount: Int,
    val follower: Int,
    val isFollow: Boolean
)
