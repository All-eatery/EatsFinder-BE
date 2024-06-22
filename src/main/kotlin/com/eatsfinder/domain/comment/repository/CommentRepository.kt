package com.eatsfinder.domain.comment.repository

import com.eatsfinder.domain.comment.model.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
}