package com.eatsfinder.domain.follow.dto

import com.eatsfinder.domain.follow.model.Follow

data class FollowingListResponse(
    val followedUserNickname: String,
    val imageUrl: String?
) {
    companion object {
        fun from(follow: Follow): FollowingListResponse {
            return FollowingListResponse(
                followedUserNickname = follow.followedUserId.nickname,
                imageUrl = follow.followedUserId.profileImage
            )
        }
    }
}