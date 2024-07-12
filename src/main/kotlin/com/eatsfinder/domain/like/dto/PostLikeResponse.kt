package com.eatsfinder.domain.like.dto

import com.eatsfinder.domain.like.model.PostLikes

data class PostLikeResponse(
    val id: Long,
    val postPlaceName: String,
    val postedUser: String
) {
    companion object {
        fun from(like: PostLikes): PostLikeResponse {
            return PostLikeResponse(
                id = like.id!!,
                postPlaceName = like.postId.placeId.name,
                postedUser = like.postId.userId.nickname
            )
        }
    }
}