package com.eatsfinder.domain.like.controller

import com.eatsfinder.domain.like.dto.CommentLikeResponse
import com.eatsfinder.domain.like.service.CommentLikeService
import com.eatsfinder.global.exception.dto.BaseResponse
import com.eatsfinder.global.security.jwt.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class CommentLikeController(
    private val commentLikeService: CommentLikeService
) {
//    @Operation(summary = "좋아요한 댓글 조회하기")
//    @GetMapping("/comment-likes")
//    fun getPostLikes(@AuthenticationPrincipal userPrincipal: UserPrincipal): ResponseEntity<List<CommentLikeResponse>> {
//        val userId = userPrincipal.id
//        return ResponseEntity.status(HttpStatus.OK).body(commentLikeService.getCommentLikes(userId))
//    }
    @Operation(summary = "댓글 좋아요 하기")
    @PostMapping("/comment-likes")
    fun createCommentLikes(@AuthenticationPrincipal userPrincipal: UserPrincipal, @RequestParam postId: Long): BaseResponse<CommentLikeResponse> {
        val userId = userPrincipal.id
        commentLikeService.createCommentLikes(userId, postId)
        return BaseResponse(message = "좋아요를 눌렸습니다.")
    }

    @Operation(summary = "댓글 좋아요 취소")
    @DeleteMapping("/comment-likes")
    fun deleteCommentLikes(@AuthenticationPrincipal userPrincipal: UserPrincipal, @RequestParam postId: Long): BaseResponse<CommentLikeResponse> {
        val userId = userPrincipal.id
        commentLikeService.deleteCommentLikes(userId, postId)
        return BaseResponse(message = "좋아요가 취소됐습니다.")
    }
}