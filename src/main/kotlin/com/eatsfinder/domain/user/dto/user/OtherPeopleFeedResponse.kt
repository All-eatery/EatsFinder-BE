package com.eatsfinder.domain.user.dto.user

data class OtherPeopleFeedResponse(
    val thumbnailUrl: String,
    val placeName: String,
    val content: String?,
    val createdAt: String
)