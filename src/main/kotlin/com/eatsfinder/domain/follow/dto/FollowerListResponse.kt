package com.eatsfinder.domain.follow.dto

import com.eatsfinder.domain.follow.model.Follow

data class FollowerListResponse(
    val followingUserNickname: String,
    val imageUrl: String?
) {
    companion object {
        fun from(follow: Follow): FollowerListResponse {
            return FollowerListResponse(
                followingUserNickname = follow.followingUserId.nickname,
                imageUrl = follow.followingUserId.profileImage
            )
        }
    }
}