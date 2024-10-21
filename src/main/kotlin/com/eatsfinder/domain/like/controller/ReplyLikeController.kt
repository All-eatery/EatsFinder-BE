package com.eatsfinder.domain.like.controller

import com.eatsfinder.domain.like.service.ReplyLikeService
import com.eatsfinder.global.exception.dto.BaseResponse
import com.eatsfinder.global.security.jwt.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ReplyLikeController(
    private val replyLikeService: ReplyLikeService
) {

    @Operation(summary = "대댓글 좋아요 하기")
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    @PostMapping("/reply-likes")
    fun createCommentLikes(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam replyId: Long
    ): BaseResponse<String> {
        val userId = userPrincipal.id
        replyLikeService.createReplyLikes(userId, replyId)
        return BaseResponse(message = "좋아요를 눌렸습니다.")
    }

    @Operation(summary = "대댓글 좋아요 취소")
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    @DeleteMapping("/reply-likes")
    fun deleteCommentLikes(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam replyId: Long
    ): BaseResponse<Unit> {
        val userId = userPrincipal.id
        replyLikeService.deleteReplyLikes(userId, replyId)
        return BaseResponse(message = "좋아요가 취소됐습니다.")
    }
}