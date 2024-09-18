package com.eatsfinder.domain.like.repository

import com.eatsfinder.domain.comment.model.Comment
import com.eatsfinder.domain.like.model.CommentLikes
import com.eatsfinder.domain.user.model.User
import com.eatsfinder.global.security.jwt.UserPrincipal
import org.springframework.data.jpa.repository.JpaRepository

interface CommentLikeRepository : JpaRepository<CommentLikes, Long> {
    fun findByUserIdAndCommentId(userId: User, commentId: Comment): CommentLikes?

//    fun findByUserId(userId: User): List<CommentLikes>

    fun findCommentLikesByUserId(userId: User): List<CommentLikes>?
}