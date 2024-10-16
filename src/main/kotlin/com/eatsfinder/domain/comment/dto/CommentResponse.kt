package com.eatsfinder.domain.comment.dto

import java.time.LocalDateTime

data class CommentResponse(
    val id: Long,
    val nickname: String,
    val profileImage: String?,
    val content: String,
    val likeCount: Int,
    val isMyComment: Boolean,
    val likeStatus: Boolean,
    val authorStatus: Boolean,
    val createdAt: LocalDateTime
)
