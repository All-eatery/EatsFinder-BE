package com.eatsfinder.domain.user.service

import com.eatsfinder.domain.user.dto.user.*
import com.eatsfinder.domain.user.dto.user.active.MyActiveResponse

interface UserService {

    fun getMyProfile(myProfileId: Long): MyProfileResponse

    fun profileViewedByOthers(otherProfileId: Long): ProfileViewedByOthersResponse

    fun updateProfile(req: UpdateProfileRequest, myProfileId: Long)

    fun defaultProfileImage(myProfileId: Long)

    fun changePassword(req: ChangePasswordRequest, myProfileId: Long)

    fun deleteProfile(
        myProfileId: Long,
        email: String,
        unavailability: Boolean,
        infrequent: Boolean,
        privacy: Boolean,
        inconvenience: Boolean,
        switching: Boolean,
        others: Boolean,
        reason: String?
    )

    fun cancelWithdrawal(myProfileId: Long)

    fun getMyFeed(myProfileId: Long) : List<MyFeedResponse>


    fun getOtherPeopleFeed(otherProfileId: Long) : List<OtherPeopleFeedResponse>

    fun getMyActive(myProfileId: Long): List<MyActiveResponse>
}