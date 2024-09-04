package com.eatsfinder.domain.comment.service

import com.eatsfinder.domain.comment.dto.CommentRequest
import com.eatsfinder.domain.comment.dto.CommentResponse
import com.eatsfinder.global.security.jwt.UserPrincipal

interface CommentService {

    fun getCommentList(postId: Long, userId: UserPrincipal?): List<CommentResponse>

    fun createComment(postId: Long, req: CommentRequest, userId: Long): String

    fun deleteComment(commentId: Long, userId: Long)
}