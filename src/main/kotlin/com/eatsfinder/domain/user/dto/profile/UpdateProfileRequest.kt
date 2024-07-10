package com.eatsfinder.domain.user.dto.profile

data class UpdateProfileRequest(
    val nickname: String?,
    val profileImage: String?,
    val phoneNumber: String?
)