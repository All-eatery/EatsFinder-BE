package com.eatsfinder.domain.notice.controller

import com.eatsfinder.domain.notice.dto.NoticeRequest
import com.eatsfinder.domain.notice.dto.NoticeResponse
import com.eatsfinder.domain.notice.service.NoticeService
import com.eatsfinder.global.exception.dto.BaseResponse
import com.eatsfinder.global.security.jwt.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
@RestController
class NoticeController(
    private val noticeService: NoticeService
) {

    @Operation(summary = "공지사항 전체 조회")
    @GetMapping("/notices")
    fun getNoticeList(
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<List<NoticeResponse>> {
        val userId = userPrincipal.id
        return ResponseEntity.status(HttpStatus.OK).body(noticeService.getNoticeList(userId))
    }

    @Operation(summary = "공지사항 단건 조회")
    @GetMapping("/notices/{noticeId}")
    fun getNotice(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable noticeId: Long
    ): ResponseEntity<NoticeResponse> {
        val userId = userPrincipal.id
        return ResponseEntity.status(HttpStatus.OK).body(noticeService.getNotice(noticeId, userId))
    }

    @Operation(summary = "공지사항 작성")
    @ApiResponse(responseCode = "403", description = "이 댓글을 수정할 권한이 없습니다.")
    @PostMapping("/notices")
    fun createNotice(
        @RequestBody req: NoticeRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): BaseResponse<String> {
        val userId = userPrincipal.id
        val message = noticeService.createNotice(req, userId)
        return BaseResponse(message = message)
    }

    @Operation(summary = "공지사항 수정")
    @ApiResponse(responseCode = "403", description = "이 댓글을 수정할 권한이 없습니다.")
    @PutMapping("/notices/{noticeId}")
    fun updateNotice(
        @PathVariable noticeId: Long,
        @RequestBody req: NoticeRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): BaseResponse<String> {
        val userId = userPrincipal.id
        val message = noticeService.updateNotice(req, userId, noticeId)
        return BaseResponse(message = message)
    }

    @Operation(summary = "공지사항 삭제")
    @ApiResponse(responseCode = "403", description = "이 댓글을 삭제할 권한이 없습니다.")
    @DeleteMapping("/notices/{noticeId}")
    fun deleteNotice(
        @PathVariable noticeId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): BaseResponse<Unit> {
        val userId = userPrincipal.id
        noticeService.deleteNotice(noticeId, userId)
        return BaseResponse(message = "공지사항이 삭제되었습니다.")
    }

}