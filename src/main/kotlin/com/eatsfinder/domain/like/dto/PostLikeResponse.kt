package com.eatsfinder.domain.like.dto

import com.eatsfinder.domain.like.model.PostLikes
import com.eatsfinder.domain.user.model.User

data class PostLikeResponse(
    val id: Long?,
    val postId: Long?,
    val postPlaceName: String,
    val postThumbnailUrl: String,
    val isPostLike: Boolean,
    val postUserNickname: String,
    val postUserProfileImage: String?
) {
    companion object {
        fun from(like: PostLikes, user: User): PostLikeResponse {
            return PostLikeResponse(
                id = like.id,
                postId = like.postId.id,
                postPlaceName = like.postId.placeId.name,
                postThumbnailUrl = like.postId.thumbnailUrl,
                isPostLike = (like.userId.id == user.id),
                postUserNickname = like.postId.userId.nickname,
                postUserProfileImage = like.postId.userId.profileImage
            )
        }
    }
}