package com.eatsfinder.domain.follow.service

interface FollowService {
    fun createUserFollow(userId: Long, followingUserId: Long)

    fun deleteUserFollow(userId: Long, unfollowingUserId: Long)
}