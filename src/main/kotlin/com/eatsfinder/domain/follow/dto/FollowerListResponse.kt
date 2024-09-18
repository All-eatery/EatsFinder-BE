package com.eatsfinder.domain.follow.dto

import com.eatsfinder.domain.follow.model.Follow

data class FollowerListResponse(
    val followerUserNickname: String,
    val imageUrl: String?
) {
    companion object {
        fun from(follow: Follow): FollowerListResponse {
            return FollowerListResponse(
                followerUserNickname = follow.followedUserId.nickname,
                imageUrl = follow.followingUserId.profileImage
            )
        }
    }
}