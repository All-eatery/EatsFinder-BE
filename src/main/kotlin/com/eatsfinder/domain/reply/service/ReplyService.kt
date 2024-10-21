package com.eatsfinder.domain.reply.service

import com.eatsfinder.domain.reply.dto.ReplyRequest

interface ReplyService {
    fun createReply(commentId: Long, req: ReplyRequest, userId: Long): String

    fun updateReply(req: ReplyRequest, userId: Long, replyId: Long): String

    fun deleteReply(replyId: Long, userId: Long)
}