package com.eatsfinder.domain.comment.controller

import com.eatsfinder.domain.comment.dto.CommentRequest
import com.eatsfinder.domain.comment.dto.CommentResponse
import com.eatsfinder.domain.comment.service.CommentService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class CommentController(
    private val commentService: CommentService
) {

    @Operation(summary = "댓글 전체 조회")
    @GetMapping("/posts/{postId}/comments")
    fun getCommentList(@PathVariable postId: Long): ResponseEntity<List<CommentResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentList(postId))
    }

    @Operation(summary = "댓글 작성")
    @PostMapping("/posts/{postId}")
    fun createComment(@PathVariable postId: Long, @RequestBody req: CommentRequest): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(postId, req))
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/comments/{commentId}")
    fun deleteComment(@PathVariable commentId: Long): ResponseEntity<Unit> {
        commentService.deleteComment(commentId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}