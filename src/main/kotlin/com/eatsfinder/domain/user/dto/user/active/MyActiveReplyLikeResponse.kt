package com.eatsfinder.domain.user.dto.user.active

data class MyActiveReplyLikeResponse (
    val replyId: Long?,
    val createdBy: MyActivePostUserResponse? = null,
    val replyContent: String?
)