package com.eatsfinder.domain.comment.dto

import com.eatsfinder.domain.comment.model.Comment
import com.eatsfinder.global.security.jwt.UserPrincipal
import java.time.LocalDateTime

data class CommentResponse(
    val id: Long,
    val nickname: String,
    val profileImage: String?,
    val content: String,
    val likeCount: Int,
    val isMyComment: Boolean,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(comment: Comment, userPrincipal: UserPrincipal?): CommentResponse {
            return CommentResponse(
                id = comment.id!!,
                nickname = comment.userId.nickname,
                profileImage = comment.userId.profileImage,
                content = comment.content,
                likeCount = comment.likeCount,
                isMyComment = (userPrincipal != null && comment.userId.id == userPrincipal.id),
                createdAt = comment.createdAt
            )
        }
    }
}
