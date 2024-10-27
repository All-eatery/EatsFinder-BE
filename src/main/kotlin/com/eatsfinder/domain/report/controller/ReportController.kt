package com.eatsfinder.domain.report.controller

import com.eatsfinder.domain.report.dto.ReportCommentRequest
import com.eatsfinder.domain.report.dto.ReportPostRequest
import com.eatsfinder.domain.report.service.ReportService
import com.eatsfinder.global.exception.dto.BaseResponse
import com.eatsfinder.global.security.jwt.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ReportController(
    private val repostService: ReportService
) {

    @Operation(summary = "게시글 신고")
    @PostMapping("/reports/posts/{postId}")
    fun createReportPost(
        @RequestBody req: ReportPostRequest,
        @PathVariable postId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): BaseResponse<String> {
        val userId = userPrincipal.id
        val message = repostService.reportPost(req, userId, postId)
        return BaseResponse(message = message)
    }

    @Operation(summary = "댓글 신고")
    @PostMapping("/reports/comments/{commentId}")
    fun createReportComment(
        @RequestBody req: ReportCommentRequest,
        @PathVariable commentId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): BaseResponse<String> {
        val userId = userPrincipal.id
        val message = repostService.reportComment(req, userId, commentId)
        return BaseResponse(message = message)
    }

    @Operation(summary = "대댓글 신고")
    @PostMapping("/reports/replies/{replyId}")
    fun createReportReply(
        @RequestBody req: ReportCommentRequest,
        @PathVariable replyId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): BaseResponse<String> {
        val userId = userPrincipal.id
        val message = repostService.reportReply(req, userId, replyId)
        return BaseResponse(message = message)
    }
}