package com.eatsfinder.domain.comment.repository

import com.eatsfinder.domain.comment.model.Comment
import com.eatsfinder.domain.post.model.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface CommentRepository : JpaRepository<Comment, Long> {

    fun findByPostIdAndDeletedAt(postId: Post, deleteAt: LocalDateTime?): List<Comment>

    fun findByIdAndDeletedAt(id: Long, deleteAt: LocalDateTime?): Comment?

    fun countByPostIdAndDeletedAt(postId: Post, deleteAt: LocalDateTime?): Int?

    @Query("SELECT c.likeCount FROM Comment c WHERE c.id = :id")
    fun getLikeCountById(@Param("id") id: Long): Int

    @Modifying
    @Query("UPDATE Comment SET likeCount = :likeCount WHERE id = :id")
    fun updateLikeCount(@Param("id") id: Long, @Param("likeCount") likeCount: Int)

    @Modifying
    @Query("DELETE FROM UserLog ul WHERE ul.commentLikeId.id = :commentLikeId")
    fun deleteUserLogCommentLikeId(@Param("commentLikeId") commentLikeId: Long)
}