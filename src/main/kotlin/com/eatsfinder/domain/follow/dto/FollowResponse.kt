package com.eatsfinder.domain.follow.dto

import com.eatsfinder.domain.follow.model.Follow

data class FollowResponse (
    val followingUserNickname: String,
    val followedUserNickname: String
){
    companion object{
        fun from(follow: Follow): FollowResponse {
            return FollowResponse(
                followingUserNickname = follow.followingUserId.nickname,
                followedUserNickname = follow.followedUserId.nickname
            )
        }
    }
}