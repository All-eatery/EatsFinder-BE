package com.eatsfinder.domain.comment.controller

import com.eatsfinder.domain.comment.dto.CommentRequest
import com.eatsfinder.domain.comment.dto.CommentResponse
import com.eatsfinder.domain.comment.service.CommentService
import com.eatsfinder.global.exception.dto.BaseResponse
import com.eatsfinder.global.security.jwt.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class CommentController(
    private val commentService: CommentService
) {

    @Operation(summary = "댓글 전체 조회")
    @GetMapping("/posts/{postId}/comments")
    fun getCommentList(@PathVariable postId: Long, @AuthenticationPrincipal userPrincipal: UserPrincipal?): ResponseEntity<List<CommentResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentList(postId, userPrincipal))
    }

    @Operation(summary = "댓글 작성")
    @PostMapping("/posts/{postId}/comments")
    fun createComment(
        @PathVariable postId: Long,
        @RequestBody req: CommentRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): BaseResponse<String> {
        val userId = userPrincipal.id
        val message = commentService.createComment(postId, req, userId)
        return BaseResponse(message = message)
    }

    @Operation(summary = "댓글 수정")
    @PutMapping("/comments/{commentId}")
    fun updateComment(
        @PathVariable commentId: Long,
        @RequestBody req: CommentRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): BaseResponse<String> {
        val userId = userPrincipal.id
        val message = commentService.updateComment(req, userId, commentId)
        return BaseResponse(message = message)
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/comments/{commentId}")
    fun deleteComment(
        @PathVariable commentId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<Unit> {
        val userId = userPrincipal.id
        commentService.deleteComment(commentId, userId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}