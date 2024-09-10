package com.eatsfinder.domain.user.service

import com.eatsfinder.domain.user.dto.user.*
import com.eatsfinder.domain.user.dto.user.active.MyActiveResponse
import com.eatsfinder.domain.user.model.DeleteUserReason

interface UserService {

    fun getMyProfile(myProfileId: Long): MyProfileResponse

    fun profileViewedByOthers(otherProfileId: Long): ProfileViewedByOthersResponse

    fun updateProfile(req: UpdateProfileRequest, myProfileId: Long)

    fun defaultProfileImage(myProfileId: Long)

    fun changePassword(req: ChangePasswordRequest, myProfileId: Long)

    fun deleteProfile(myProfileId: Long, req: DeleteReasonRequest, reasonType: DeleteUserReason)

    fun getMyFeed(myProfileId: Long) : List<MyFeedResponse>

    fun getMyActive(myProfileId: Long): List<MyActiveResponse>
}