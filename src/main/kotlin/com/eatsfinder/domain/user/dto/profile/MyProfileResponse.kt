package com.eatsfinder.domain.user.dto.profile

import com.eatsfinder.domain.user.model.User

data class MyProfileResponse(
    val id: Long,
    val nickname: String,
    val email: String,
    val phoneNumber: String,
    val profileImage: String?,
    val followingCount: Int,
    val followerCount: Int
) {
    companion object {
        fun from(profile: User): MyProfileResponse {
            return MyProfileResponse(
                id = profile.id!!,
                nickname = profile.nickname,
                email = profile.email,
                phoneNumber = profile.phoneNumber,
                profileImage = profile.profileImage,
                followingCount = profile.followingCount,
                followerCount = profile.followerCount
            )
        }
    }
}

