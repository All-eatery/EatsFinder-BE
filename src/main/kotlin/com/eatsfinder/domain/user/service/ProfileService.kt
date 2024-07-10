package com.eatsfinder.domain.user.service

import com.eatsfinder.domain.user.dto.profile.MyProfileResponse
import com.eatsfinder.domain.user.dto.profile.ProfileViewedByOthersResponse
import com.eatsfinder.domain.user.dto.profile.UpdateProfileRequest

interface ProfileService {

    fun getMyProfile(profileId: Long): MyProfileResponse

    fun profileViewedByOthers(profileId: Long): ProfileViewedByOthersResponse

    fun updateProfile(req: UpdateProfileRequest, profileId: Long)

    fun deleteProfile(profileId: Long, email: String, code: String)
}