package com.eatsfinder.domain.follow.service

import com.eatsfinder.domain.follow.dto.FollowResponse
import com.eatsfinder.domain.follow.dto.FollowerListResponse
import com.eatsfinder.domain.follow.dto.FollowingListResponse

interface FollowService {


    fun checkFollowing(userId: Long, followUserId: Long): FollowResponse

    fun createUserFollow(userId: Long, followUserId: Long)

    fun deleteUserFollow(userId: Long, unfollowUserId: Long)

    fun getFollowingList(userId: Long): List<FollowingListResponse>

    fun getFollowerList(userId: Long): List<FollowerListResponse>
}