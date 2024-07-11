package com.eatsfinder.domain.user.dto.profile

import org.springframework.web.multipart.MultipartFile

data class UpdateProfileRequest(
    val nickname: String?,
    val profileImage: MultipartFile?,
    val phoneNumber: String?
)