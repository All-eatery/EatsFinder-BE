package com.eatsfinder.domain.like.dto

import com.eatsfinder.domain.like.model.CommentLikes

data class CommentLikeResponse(
    val id: Long,
    val postPlaceName: String,
    val postedUser: String,
    val commentedUser: String,
    val commentContent: String,
    val commentLikesCount: Int
) {
    companion object {
        fun from(like: CommentLikes): CommentLikeResponse {
            return CommentLikeResponse(
                id = like.id!!,
                postPlaceName = like.commentId.postId.placeId.name,
                postedUser = like.commentId.postId.userId.nickname,
                commentedUser = like.commentId.userId.nickname,
                commentContent = like.commentId.content,
                commentLikesCount = like.commentId.likeCount
            )
        }
    }
}
