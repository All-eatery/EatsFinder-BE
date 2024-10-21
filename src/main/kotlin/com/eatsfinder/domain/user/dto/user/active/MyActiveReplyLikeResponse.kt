package com.eatsfinder.domain.user.dto.user.active

data class MyActiveReplyLikeResponse (
    val replyId: Long?,
    val createdBy: MyActiveReplyUserResponse? = null,
    val replyContent: String?
)