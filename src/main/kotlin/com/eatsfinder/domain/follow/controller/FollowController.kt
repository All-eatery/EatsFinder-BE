package com.eatsfinder.domain.follow.controller

import com.eatsfinder.domain.follow.dto.FollowResponse
import com.eatsfinder.domain.follow.dto.FollowerListResponse
import com.eatsfinder.domain.follow.dto.FollowingListResponse
import com.eatsfinder.domain.follow.service.FollowService
import com.eatsfinder.global.exception.dto.BaseResponse
import com.eatsfinder.global.security.jwt.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class FollowController(
    private val followService: FollowService
) {

    @Operation(summary = "팔로우 확인")
    @GetMapping("/follows")
    fun checkFollowing(@AuthenticationPrincipal userPrincipal: UserPrincipal, @RequestParam followUserId: Long): ResponseEntity<FollowResponse>{
        val userId = userPrincipal.id
        return ResponseEntity.status(HttpStatus.OK).body(followService.checkFollowing(userId, followUserId))
    }

    @Operation(summary = "유저 팔로우 하기")
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    @PostMapping("/follows")
    fun createUserFollow(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam followUserId: Long
    ): BaseResponse<FollowResponse> {
        val userId = userPrincipal.id
        followService.createUserFollow(userId, followUserId)
        return BaseResponse(message = "팔로우를 하였습니다.")
    }

    @Operation(summary = "유저 언팔로우 하기")
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    @DeleteMapping("/follows")
    fun deleteUserFollow(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam unfollowUserId: Long
    ): BaseResponse<Unit> {
        val userId = userPrincipal.id
        followService.deleteUserFollow(userId, unfollowUserId)
        return BaseResponse(message = "언팔로우를 하였습니다.")
    }

    @Operation(summary = "팔로잉 확인")
    @GetMapping("/following")
    fun getFollowingList(@RequestParam userId: Long): ResponseEntity<List<FollowingListResponse>>{
        return ResponseEntity.status(HttpStatus.OK).body(followService.getFollowingList(userId))
    }

    @Operation(summary = "팔로워 확인")
    @GetMapping("/follower")
    fun getFollowerList(@RequestParam userId: Long): ResponseEntity<List<FollowerListResponse>>{
        return ResponseEntity.status(HttpStatus.OK).body(followService.getFollowerList(userId))
    }

}