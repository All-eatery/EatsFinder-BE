package com.eatsfinder.domain.follow.dto

import com.eatsfinder.domain.follow.model.Follow

data class FollowingListResponse(
    val followingUserId: Long,
    val followingUserNickname: String,
    val imageUrl: String?,
    val isFollow: Boolean
) {
    companion object {
        fun from(follow: Follow, isFollow: Boolean): FollowingListResponse {
            return FollowingListResponse(
                followingUserId = follow.followingUserId.id!!,
                followingUserNickname = follow.followingUserId.nickname,
                imageUrl = follow.followedUserId.profileImage,
                isFollow = isFollow
            )
        }
    }
}