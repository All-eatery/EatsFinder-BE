package com.eatsfinder.domain.user.dto.user

data class MyFeedResponse(
    val postId: Long?,
    val thumbnailUrl: String,
    val placeName: String,
    val content: String?,
    val createdAt: String
)
