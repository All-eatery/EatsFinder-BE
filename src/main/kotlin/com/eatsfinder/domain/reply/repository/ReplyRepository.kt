package com.eatsfinder.domain.reply.repository

import com.eatsfinder.domain.comment.model.Comment
import com.eatsfinder.domain.reply.model.Reply
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface ReplyRepository : JpaRepository<Reply, Long> {

    fun findByIdAndDeletedAt(id: Long, deleteAt: LocalDateTime?): Reply?
}