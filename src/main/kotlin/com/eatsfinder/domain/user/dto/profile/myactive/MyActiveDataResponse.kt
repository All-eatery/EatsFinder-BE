package com.eatsfinder.domain.user.dto.profile.myactive

data class MyActiveDataResponse(
    val postLikeId: Long?,
    val commentLikeId: Long?,
    val comment: MyActiveCommentResponse,
    val createdAt: String
)
