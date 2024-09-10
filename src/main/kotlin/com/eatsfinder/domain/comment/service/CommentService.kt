package com.eatsfinder.domain.comment.service

import com.eatsfinder.domain.comment.dto.CommentRequest
import com.eatsfinder.domain.comment.dto.CommentsResponse
import com.eatsfinder.global.security.jwt.UserPrincipal

interface CommentService {

    fun getCommentList(postId: Long, userId: UserPrincipal?): CommentsResponse

    fun createComment(postId: Long, req: CommentRequest, userId: Long): String

    fun updateComment(req: CommentRequest, userId: Long, commentId: Long): String

    fun deleteComment(commentId: Long, userId: Long)
}