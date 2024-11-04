package com.eatsfinder.domain.user.dto.user.active

import java.time.LocalDateTime

data class MyActiveReplyResponse(
    val id: Long?,
    val commentId: Long?,
    val commentDeletedAt: LocalDateTime?,
    val createdBy: MyActiveCommentUserResponse? = null,
    val content: String?
)
