package com.eatsfinder.domain.post.dto

data class TopPostResponse(
    val postId: Long,
    val placeName: String,
    val postThumbnailUrl: String,
    val isPostLike: Boolean,
    val postLikeCount: Int,
    val profileImage: String?,
    val nickname: String
)
