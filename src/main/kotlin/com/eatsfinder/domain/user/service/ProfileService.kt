package com.eatsfinder.domain.user.service

import com.eatsfinder.domain.user.dto.profile.*
import com.eatsfinder.domain.user.model.MyActiveType

interface ProfileService {

    fun getMyProfile(myProfileId: Long): MyProfileResponse

    fun profileViewedByOthers(profileId: Long): ProfileViewedByOthersResponse

    fun updateProfile(req: UpdateProfileRequest, myProfileId: Long)

    fun defaultProfileImage(myProfileId: Long)

    fun changePassword(req: ChangePasswordRequest, myProfileId: Long)

    fun deleteProfile(myProfileId: Long, email: String, code: String)

    fun getMyFeed(myProfileId: Long) : List<MyFeedResponse>

    fun getMyActive(myProfileId: Long): List<MyActiveResponse>
}