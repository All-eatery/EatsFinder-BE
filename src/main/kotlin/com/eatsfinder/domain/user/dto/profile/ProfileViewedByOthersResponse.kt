package com.eatsfinder.domain.user.dto.profile

import com.eatsfinder.domain.user.model.User

data class ProfileViewedByOthersResponse(
    val id: Long,
    val nickname: String,
    val email: String,
    val profileImage: String?,
    val followingCount: Int,
    val followerCount: Int
) {
    companion object {
        fun from(profile: User): ProfileViewedByOthersResponse {
            return ProfileViewedByOthersResponse(
                id = profile.id!!,
                nickname = profile.nickname,
                email = profile.email,
                profileImage = profile.profileImage,
                followingCount = profile.followingCount,
                followerCount = profile.followerCount
            )
        }
    }
}
