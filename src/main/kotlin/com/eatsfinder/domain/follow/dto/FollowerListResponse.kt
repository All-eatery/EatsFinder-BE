package com.eatsfinder.domain.follow.dto

import com.eatsfinder.domain.follow.model.Follow

data class FollowerListResponse(
    val followerUserId: Long,
    val followerUserNickname: String,
    val imageUrl: String?,
    val isFollow: Boolean
) {
    companion object {
        fun from(follow: Follow, isFollow: Boolean): FollowerListResponse {
            return FollowerListResponse(
                followerUserId = follow.followedUserId.id!!,
                followerUserNickname = follow.followedUserId.nickname,
                imageUrl = follow.followingUserId.profileImage,
                isFollow = isFollow
            )
        }
    }
}