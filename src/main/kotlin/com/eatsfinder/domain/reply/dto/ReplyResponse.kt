package com.eatsfinder.domain.reply.dto

import java.time.LocalDateTime

data class ReplyResponse (
    val id: Long,
    val nickname: String,
    val profileImage: String?,
    val content: String,
    val likeCount: Int,
    val isMyComment: Boolean,
    val likeStatus: Boolean,
    val authorStatus: Boolean,
    val isUpdated: Boolean,
    val createdAt: LocalDateTime
)