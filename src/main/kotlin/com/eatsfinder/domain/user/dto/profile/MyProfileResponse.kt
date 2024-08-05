package com.eatsfinder.domain.user.dto.profile

import com.eatsfinder.domain.user.model.SocialType
import com.eatsfinder.domain.user.model.User

data class MyProfileResponse(
    val id: Long,
    val nickname: String,
    val email: String,
    val phoneNumber: String,
    val profileImage: String?,
    val userType: SocialType,
    val followingCount: Int,
    val followerCount: Int,
    val postCount: Int = 0
) {
    companion object {
        fun from(profile: User, postCount: Int): MyProfileResponse {
            return MyProfileResponse(
                id = profile.id!!,
                nickname = profile.nickname,
                email = profile.email,
                phoneNumber = profile.phoneNumber,
                profileImage = profile.profileImage,
                userType = profile.provider,
                followingCount = profile.followingCount,
                followerCount = profile.followerCount,
                postCount = postCount
            )
        }
    }
}

