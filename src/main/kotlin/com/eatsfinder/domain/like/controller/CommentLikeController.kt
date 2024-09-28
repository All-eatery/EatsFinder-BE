package com.eatsfinder.domain.like.controller

import com.eatsfinder.domain.like.dto.CommentLikeResponse
import com.eatsfinder.domain.like.service.CommentLikeService
import com.eatsfinder.global.exception.dto.BaseResponse
import com.eatsfinder.global.security.jwt.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class CommentLikeController(
    private val commentLikeService: CommentLikeService
) {

    @Operation(summary = "댓글 좋아요 하기")
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    @PostMapping("/comment-likes")
    fun createCommentLikes(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam commentId: Long
    ): BaseResponse<CommentLikeResponse> {
        val userId = userPrincipal.id
        commentLikeService.createCommentLikes(userId, commentId)
        return BaseResponse(message = "좋아요를 눌렸습니다.")
    }

    @Operation(summary = "댓글 좋아요 취소")
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    @DeleteMapping("/comment-likes")
    fun deleteCommentLikes(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam commentId: Long
    ): BaseResponse<Unit> {
        val userId = userPrincipal.id
        commentLikeService.deleteCommentLikes(userId, commentId)
        return BaseResponse(message = "좋아요가 취소됐습니다.")
    }
}