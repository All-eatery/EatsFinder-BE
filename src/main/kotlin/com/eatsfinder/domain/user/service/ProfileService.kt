package com.eatsfinder.domain.user.service

import com.eatsfinder.domain.user.dto.profile.ChangePasswordRequest
import com.eatsfinder.domain.user.dto.profile.MyProfileResponse
import com.eatsfinder.domain.user.dto.profile.ProfileViewedByOthersResponse
import com.eatsfinder.domain.user.dto.profile.UpdateProfileRequest

interface ProfileService {

    fun getMyProfile(myProfileId: Long): MyProfileResponse

    fun profileViewedByOthers(profileId: Long, myProfileId: Long): ProfileViewedByOthersResponse

    fun updateProfile(req: UpdateProfileRequest, myProfileId: Long)

    fun changePassword(req: ChangePasswordRequest, myProfileId: Long)

    fun deleteProfile(myProfileId: Long, email: String, code: String)
}