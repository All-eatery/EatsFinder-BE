package com.eatsfinder.domain.follow.dto

import com.eatsfinder.domain.follow.model.Follow

data class FollowResponse(
    val followingUserId: Long,
    val followingUserNickname: String,
    val followedUserId: Long,
    val followedUserNickname: String
) {
    companion object {
        fun from(follow: Follow): FollowResponse {
            return FollowResponse(
                followingUserId = follow.followingUserId.id!!,
                followingUserNickname = follow.followingUserId.nickname,
                followedUserId = follow.followedUserId.id!!,
                followedUserNickname = follow.followedUserId.nickname
            )
        }
    }
}