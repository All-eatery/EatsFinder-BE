package com.eatsfinder.domain.follow.service

import com.eatsfinder.domain.follow.dto.FollowResponse

interface FollowService {


    fun checkFollowing(userId: Long, followUserId: Long): FollowResponse

    fun createUserFollow(userId: Long, followUserId: Long)

    fun deleteUserFollow(userId: Long, unfollowUserId: Long)

    fun getFollowingList(userId: Long): List<FollowResponse>

    fun getFollowerList(userId: Long): List<FollowResponse>
}