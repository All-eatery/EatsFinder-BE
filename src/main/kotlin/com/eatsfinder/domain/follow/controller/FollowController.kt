package com.eatsfinder.domain.follow.controller

import com.eatsfinder.domain.follow.dto.FollowResponse
import com.eatsfinder.domain.follow.service.FollowService
import com.eatsfinder.global.exception.dto.BaseResponse
import com.eatsfinder.global.security.jwt.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class FollowController(
    private val followService: FollowService
) {

    @Operation(summary = "유저 팔로우 하기")
    @PostMapping("/follow")
    fun createUserFollow(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam followingUserId: Long
    ): BaseResponse<FollowResponse> {
        val userId = userPrincipal.id
        followService.createUserFollow(userId, followingUserId)
        return BaseResponse(message = "팔로우를 하였습니다.")
    }

    @Operation(summary = "유저 언팔로우 하기")
    @DeleteMapping("/follow")
    fun deleteUserFollow(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam unfollowingUserId: Long
    ): BaseResponse<FollowResponse> {
        val userId = userPrincipal.id
        followService.deleteUserFollow(userId, unfollowingUserId)
        return BaseResponse(message = "언팔로우를 하였습니다.")
    }

}