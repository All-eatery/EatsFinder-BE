package com.eatsfinder.domain.like.controller

import com.eatsfinder.domain.like.dto.PostLikesResponse
import com.eatsfinder.domain.like.service.PostLikeService
import com.eatsfinder.global.exception.dto.BaseResponse
import com.eatsfinder.global.security.jwt.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class PostLikeController(
    private val postLikeService: PostLikeService
) {

    @Operation(summary = "좋아요한 게시물 조회하기")
    @GetMapping("/post-likes")
    fun getPostLikes(@AuthenticationPrincipal userPrincipal: UserPrincipal): ResponseEntity<PostLikesResponse> {
        val userId = userPrincipal.id
        return ResponseEntity.status(HttpStatus.OK).body(postLikeService.getPostLikes(userId))
    }

    @Operation(summary = "게시물 좋아요 하기")
    @PostMapping("/post-likes")
    fun createPostLikes(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam postId: Long
    ): BaseResponse<PostLikesResponse> {
        val userId = userPrincipal.id
        postLikeService.createPostLikes(userId, postId)
        return BaseResponse(message = "좋아요를 눌렸습니다.")
    }

    @Operation(summary = "게시물 좋아요 취소")
    @DeleteMapping("/post-likes")
    fun deletePostLikes(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam postId: Long
    ): BaseResponse<PostLikesResponse> {
        val userId = userPrincipal.id
        postLikeService.deletePostLikes(userId, postId)
        return BaseResponse(message = "좋아요가 취소됐습니다.")
    }
}