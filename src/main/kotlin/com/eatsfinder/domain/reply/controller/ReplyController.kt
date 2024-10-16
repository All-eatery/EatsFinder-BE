package com.eatsfinder.domain.reply.controller

import com.eatsfinder.domain.reply.dto.ReplyRequest
import com.eatsfinder.domain.reply.service.ReplyService
import com.eatsfinder.global.exception.dto.BaseResponse
import com.eatsfinder.global.security.jwt.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class ReplyController(
    private val replyService: ReplyService
) {
    @Operation(summary = "대댓글 작성")
    @PostMapping("/comments/{commentId}/replies")
    fun createReply(
        @PathVariable commentId: Long,
        @RequestBody req: ReplyRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): BaseResponse<String> {
        val userId = userPrincipal.id
        val message = replyService.createReply(commentId, req, userId)
        return BaseResponse(message = message)
    }

    @Operation(summary = "대댓글 수정")
    @PutMapping("/replies/{replyId}")
    fun updateReply(
        @PathVariable replyId: Long,
        @RequestBody req: ReplyRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): BaseResponse<String> {
        val userId = userPrincipal.id
        val message = replyService.updateReply(req, userId, replyId)
        return BaseResponse(message = message)
    }

    @Operation(summary = "대댓글 삭제")
    @DeleteMapping("/replies/{replyId}")
    fun deleteReply(
        @PathVariable replyId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): BaseResponse<Unit> {
        val userId = userPrincipal.id
        replyService.deleteReply(replyId, userId)
        return BaseResponse(message = "대댓글이 삭제되었습니다.")
    }
}