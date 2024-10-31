package com.eatsfinder.domain.user.service

import com.eatsfinder.domain.user.dto.user.*
import com.eatsfinder.domain.user.dto.user.active.MyActiveResponse
import com.eatsfinder.domain.user.model.MyActiveFilter
import org.springframework.data.domain.Pageable

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

    fun getMyFeed(myProfileId: Long, pageable: Pageable) : MyFeedsResponse


    fun getOtherPeopleFeed(otherProfileId: Long, pageable: Pageable, userId: Long?) : OtherPeopleFeedsResponse

    fun getMyActive(myProfileId: Long, pageable: Pageable, filter : MyActiveFilter): List<MyActiveResponse>
}