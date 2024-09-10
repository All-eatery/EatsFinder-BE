package com.eatsfinder.domain.like.dto

data class PostLikeResponse(
    val id: Long?,
    val postId: Long?,
    val postPlaceName: String,
    val postThumbnailUrl: String,
    val isPostLike: Boolean,
    val postUserNickname: String,
    val postUserProfileImage: String?
)
