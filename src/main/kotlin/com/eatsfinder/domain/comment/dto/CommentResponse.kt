package com.eatsfinder.domain.comment.dto

import com.eatsfinder.domain.comment.model.Comment
import java.time.LocalDateTime

data class CommentResponse(
    val id: Long,
    val nickname: String,
    val profileImage: String,
    val content: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(comment: Comment): CommentResponse {
            return CommentResponse(
                id = comment.id!!,
                nickname = comment.userId.nickname,
                profileImage = comment.userId.profileImage!!,
                content = comment.content,
                createdAt = comment.createdAt
            )
        }
    }
}
