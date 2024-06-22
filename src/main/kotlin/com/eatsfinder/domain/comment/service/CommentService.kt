package com.eatsfinder.domain.comment.service

import com.eatsfinder.domain.comment.dto.CommentRequest
import com.eatsfinder.domain.comment.dto.CommentResponse

interface CommentService {

    fun getCommentList(postId: Long): List<CommentResponse>

    fun createComment(req: CommentRequest): String

    fun deleteComment(commentId: Long)
}