package com.eatsfinder.domain.user.dto.user.active

data class MyActivePostLikeResponse (
    val postId: Long?,
    val createdBy: MyActivePostUserResponse? = null,
    val postContent: String?
)