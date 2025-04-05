package com.eatsfinder.domain.search.dto

data class NeighborDataResponse(
    val id: Long,
    val nickname: String,
    val profileImage: String?,
    val postCount: Int,
    val followerCount: Int
)

