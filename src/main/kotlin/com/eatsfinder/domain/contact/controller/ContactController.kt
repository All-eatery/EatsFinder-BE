package com.eatsfinder.domain.contact.controller

import com.eatsfinder.domain.contact.dto.ARequest
import com.eatsfinder.domain.contact.dto.QRequest
import com.eatsfinder.domain.contact.dto.QResponse
import com.eatsfinder.domain.contact.service.ContactService
import com.eatsfinder.domain.notice.dto.NoticeRequest
import com.eatsfinder.domain.notice.dto.NoticeResponse
import com.eatsfinder.global.exception.dto.BaseResponse
import com.eatsfinder.global.security.jwt.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class ContactController(
    private val contactService: ContactService
) {

    @Operation(summary = "문의사항 전체 조회")
    @GetMapping("/queries")
    fun getQueryList(
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<List<QResponse>> {
        val userId = userPrincipal.id
        return ResponseEntity.status(HttpStatus.OK).body(contactService.getQueryList(userId))
    }

    @Operation(summary = "문의사항 단건 조회")
    @GetMapping("/queries/{queryId}")
    fun getQuery(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable queryId: Long
    ): ResponseEntity<QResponse> {
        val userId = userPrincipal.id
        return ResponseEntity.status(HttpStatus.OK).body(contactService.getQuery(queryId, userId))
    }

    @Operation(summary = "문의사항 작성")
    @ApiResponse(responseCode = "403", description = "이 문의사항을 작성할 권한이 없습니다.")
    @PostMapping("/queries")
    fun createQuery(
        @RequestBody req: NoticeRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): BaseResponse<String> {
        val userId = userPrincipal.id
        val message = contactService.createQuery(req, userId)
        return BaseResponse(message = message)
    }

    @Operation(summary = "문의사항 수정")
    @ApiResponse(responseCode = "403", description = "이 문의사항을 수정할 권한이 없습니다.")
    @PutMapping("/queries/{queryId}")
    fun updateQuery(
        @PathVariable queryId: Long,
        @RequestBody req: QRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): BaseResponse<String> {
        val userId = userPrincipal.id
        val message = contactService.updateQuery(req, userId, queryId)
        return BaseResponse(message = message)
    }

    @Operation(summary = "문의사항 삭제")
    @ApiResponse(responseCode = "403", description = "이 문의사항을 삭제할 권한이 없습니다.")
    @DeleteMapping("/queries/{queryId}")
    fun deleteQuery(
        @PathVariable queryId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): BaseResponse<Unit> {
        val userId = userPrincipal.id
        contactService.deleteQuery(queryId, userId)
        return BaseResponse(message = "공지사항이 삭제되었습니다.")
    }

    @Operation(summary = "답변 작성")
    @ApiResponse(responseCode = "403", description = "이 답변을 작성할 권한이 없습니다.")
    @PostMapping("/answers")
    fun createAnswer(
        @RequestBody req: NoticeRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): BaseResponse<String> {
        val userId = userPrincipal.id
        val message = contactService.createAnswer(req, userId)
        return BaseResponse(message = message)
    }

    @Operation(summary = "답변 수정")
    @ApiResponse(responseCode = "403", description = "이 답변을 수정할 권한이 없습니다.")
    @PutMapping("/answers/{answerId}")
    fun updateAnswer(
        @PathVariable answerId: Long,
        @RequestBody req: ARequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): BaseResponse<String> {
        val userId = userPrincipal.id
        val message = contactService.updateAnswer(req, userId, answerId)
        return BaseResponse(message = message)
    }

    @Operation(summary = "답변 삭제")
    @ApiResponse(responseCode = "403", description = "이 답변을 삭제할 권한이 없습니다.")
    @DeleteMapping("/answers/{answerId}")
    fun deleteAnswer(
        @PathVariable answerId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): BaseResponse<Unit> {
        val userId = userPrincipal.id
        contactService.deleteAnswer(answerId, userId)
        return BaseResponse(message = "공지사항이 삭제되었습니다.")
    }
}