package com.eatsfinder.domain.user.dto.profile.myactive

data class MyActiveDataResponse(
    val type: String,
    val postLikeId: MyActivePostLikeResponse,
    val commentLikeId: MyActiveCommentLikeResponse,
    val comment: MyActiveCommentResponse,
    val createdAt: String
)
