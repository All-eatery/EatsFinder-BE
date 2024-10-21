package com.eatsfinder.domain.like.service

interface ReplyLikeService {

    fun createReplyLikes(userId: Long, replyId: Long)

    fun deleteReplyLikes(userId: Long, replyId: Long)

}