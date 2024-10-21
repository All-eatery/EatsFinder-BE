package com.eatsfinder.domain.user.dto.user.active

data class MyActiveDataResponse(
    val type: String,
    val postLike: MyActivePostLikeResponse?,
    val commentLike: MyActiveCommentLikeResponse?,
    val comment: MyActiveCommentResponse?,
    val reply:MyActiveReplyResponse?,
    val replyLike: MyActiveReplyLikeResponse?,
    val createdAt: String
)
