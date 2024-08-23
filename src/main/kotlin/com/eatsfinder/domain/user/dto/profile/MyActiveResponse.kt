package com.eatsfinder.domain.user.dto.profile

import com.eatsfinder.domain.user.model.UserLog


data class MyActiveResponse(
    val postLikes: Long?,
    val commentLikes: Long?,
    val comment: String?
){
    companion object{
        fun from(log: UserLog): MyActiveResponse {

            return MyActiveResponse(
                postLikes = log.postLikeId?.id,
                commentLikes = log.commentLikeId?.id,
                comment = log.commentId?.content,
            )
        }
    }
}
