package com.eatsfinder.domain.post.dto

data class NeighborPostResponse (
    val followingUser: FollowingUserDataResponse,
    val placeName: String,
    val postId: Long?,
    val postThumbnailUrl: String,
    val postLikeCount: Int
)