package com.eatsfinder.domain.search.dto

import com.eatsfinder.domain.user.model.User

data class NeighborPostResponse(
    val neighbor: NeighborDataResponse,
    val isFollow: Boolean
) {
    companion object {
        fun from(user: User, postCount: Int, isFollow: Boolean): NeighborPostResponse {
            return NeighborPostResponse(
                neighbor = NeighborDataResponse(
                    id = user.id!!,
                    nickname = user.nickname,
                    profileImage = user.profileImage,
                    postCount = postCount,
                    followerCount = user.followerCount,
                ),
                isFollow = isFollow,
            )
        }
    }
}
