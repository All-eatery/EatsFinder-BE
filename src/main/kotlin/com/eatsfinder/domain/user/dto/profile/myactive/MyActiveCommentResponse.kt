package com.eatsfinder.domain.user.dto.profile.myactive

import java.time.LocalDateTime

data class MyActiveCommentResponse(
    val id: Long?,
    val postId: Long?,
    val postDeletedAt: LocalDateTime?,
    val createdBy: MyActivePostUserResponse? = null,
    val content: String?
)
