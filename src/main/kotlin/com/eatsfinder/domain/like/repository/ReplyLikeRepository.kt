package com.eatsfinder.domain.like.repository


import com.eatsfinder.domain.like.model.ReplyLikes
import com.eatsfinder.domain.reply.model.Reply
import com.eatsfinder.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface ReplyLikeRepository : JpaRepository<ReplyLikes, Long> {

    fun findByUserIdAndReplyId(userId: User, replyId: Reply): ReplyLikes?
}