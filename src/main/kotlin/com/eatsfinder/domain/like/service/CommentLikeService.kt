package com.eatsfinder.domain.like.service


interface CommentLikeService {

    fun createCommentLikes(userId: Long, commentId: Long)

    fun deleteCommentLikes(userId: Long, commentId: Long)

}