package com.eatsfinder.domain.comment.repository

import com.eatsfinder.domain.comment.model.Comment
import com.eatsfinder.domain.post.model.Post
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface CommentRepository : JpaRepository<Comment, Long> {

    fun findByPostIdAndDeletedAt(postId: Post, deleteAt: LocalDateTime?): List<Comment>

    fun findByIdAndDeletedAt(id: Long, deleteAt: LocalDateTime?): Comment?
}