package com.eatsfinder.domain.user.dto.profile.myactive

data class MyActivePostLikeResponse (
    val postId: Long?,
    val createdBy: MyActivePostUserResponse? = null,
    val postContent: String?
)