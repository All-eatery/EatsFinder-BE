package com.eatsfinder.domain.user.dto.profile.myactive

data class MyActiveCommentResponse(
    val id: Long?,
    val postId: Long?,
    val createdBy: MyActivePostUserResponse? = null,
    val content: String?
)
