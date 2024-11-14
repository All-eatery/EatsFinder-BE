package com.eatsfinder.domain.search.dto

import java.time.LocalDateTime

data class PostSearchResponse(
    val userId: Long,
    val userImageUrl: String,
    val placeName: String,
    val postId: Long?,
    val postThumbnailUrl: String,
    val isPostLike: Boolean,
    val postLikeCount: Int,
    val updatedAt: LocalDateTime
)