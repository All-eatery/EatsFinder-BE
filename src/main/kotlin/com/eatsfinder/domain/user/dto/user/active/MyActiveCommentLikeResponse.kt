package com.eatsfinder.domain.user.dto.user.active

data class MyActiveCommentLikeResponse(
    val postId: Long?,
    val commentId: Long?,
    val createdBy: MyActiveCommentUserResponse? = null,
    val commentContent: String?
)
