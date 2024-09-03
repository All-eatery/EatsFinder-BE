package com.eatsfinder.domain.post.dto

import java.time.LocalDateTime

data class NeighborPostResponse (
    val followingUser: FollowingUserDataResponse,
    val placeName: String,
    val postId: Long?,
    val postThumbnailUrl: String,
    val isPostLike: Boolean,
    val postLikeCount: Int,
    val updatedAt: LocalDateTime
)