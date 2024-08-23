package com.eatsfinder.domain.user.dto.profile.myactive

data class MyActiveCommentLikeResponse(
    val postId: Long?,
    val createBy: MyActiveCommentUserResponse? = null,
    val commentContent: String?
)
