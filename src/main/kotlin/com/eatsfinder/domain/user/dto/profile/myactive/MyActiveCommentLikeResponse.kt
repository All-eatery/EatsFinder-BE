package com.eatsfinder.domain.user.dto.profile.myactive

data class MyActiveCommentLikeResponse(
    val postId: Long?,
    val commentId: Long?,
    val createdBy: MyActiveCommentUserResponse? = null,
    val commentContent: String?
)
