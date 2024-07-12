package com.eatsfinder.domain.like.service

import com.eatsfinder.domain.like.dto.CommentLikeResponse

interface CommentLikeService {

    fun createCommentLikes(userId: Long, commentId: Long)

    fun deleteCommentLikes(userId: Long, commentId: Long)

    fun getCommentLikes(userId: Long): List<CommentLikeResponse>
}