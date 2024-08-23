package com.eatsfinder.domain.user.dto.profile.myactive

data class MyActiveDataResponse(
    val type: String,
    val postLike: MyActivePostLikeResponse?,
    val commentLike: MyActiveCommentLikeResponse?,
    val comment: MyActiveCommentResponse?,
    val createdAt: String
)
