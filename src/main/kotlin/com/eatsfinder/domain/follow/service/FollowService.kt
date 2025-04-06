package com.eatsfinder.domain.follow.service

import com.eatsfinder.domain.follow.dto.*
import com.eatsfinder.domain.follow.model.Follow
import com.eatsfinder.domain.user.model.User
import com.eatsfinder.global.pagination.PaginationItemsResponse
import com.eatsfinder.global.security.jwt.UserPrincipal

interface FollowService {


    fun checkFollowing(userId: Long, followUserId: Long): FollowResponse

    fun createUserFollow(userId: Long, followUserId: Long)

    fun deleteUserFollow(userId: Long, followUserId: Long)

    fun findFollowingListCursorBased(cursorId: Long?, pageSize: Int, userId: Long): PaginationFollowingResponse

    fun findFollowerListCursorBased(cursorId: Long?, pageSize: Int, userId: Long): PaginationFollowerResponse
}