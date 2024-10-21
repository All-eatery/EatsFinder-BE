package com.eatsfinder.domain.reply.repository

import com.eatsfinder.domain.reply.model.Reply
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface ReplyRepository : JpaRepository<Reply, Long> {

    @Query("SELECT r.likeCount FROM Reply r WHERE r.id = :id")
    fun getLikeCountById(@Param("id") id: Long): Int

    fun findByIdAndDeletedAt(id: Long, deleteAt: LocalDateTime?): Reply?

    @Modifying
    @Query("UPDATE Reply SET likeCount = :likeCount WHERE id = :id")
    fun updateLikeCount(@Param("id") id: Long, @Param("likeCount") likeCount: Int)

    @Modifying
    @Query("DELETE FROM UserLog ul WHERE ul.replyLikeId.id = :replyLikeId")
    fun deleteUserLogReplyLikeId(@Param("replyLikeId") replyLikeId: Long)
}