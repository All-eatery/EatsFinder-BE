package com.eatsfinder.domain.search.dto

import com.eatsfinder.domain.user.model.User

data class NeighborPostResponse(
    val imageUrl: String?,
    val nickname: String,
    val postCount: Int,
    val follower: Int,
    val isFollow: Boolean
) {
    companion object {
        fun from(user: User, postCount: Int, isFollow: Boolean): NeighborPostResponse {
            return NeighborPostResponse(
                imageUrl = user.profileImage,
                nickname = user.nickname,
                postCount = postCount,
                follower = user.followerCount,
                isFollow = isFollow
            )
        }
    }
}
