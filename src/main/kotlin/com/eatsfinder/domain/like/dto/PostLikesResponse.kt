package com.eatsfinder.domain.like.dto

import com.eatsfinder.domain.like.model.PostLikes
import com.eatsfinder.domain.user.model.User

data class PostLikesResponse(
    val totalPostCount: Int? = 0,
    val posts: List<PostLikeResponse>
) {
    companion object {
        fun from(likes: List<PostLikes>, user: User, postCount: Int): PostLikesResponse {
            val res = likes.map { like ->
                PostLikeResponse(
                    id = like.id,
                    postId = like.postId.id,
                    postPlaceName = like.postId.placeId.name,
                    postThumbnailUrl = like.postId.thumbnailUrl,
                    isPostLike = (like.userId.id == user.id),
                    postUserNickname = like.postId.userId.nickname,
                    postUserProfileImage = like.postId.userId.profileImage
                )
            }
            return PostLikesResponse(
                totalPostCount = postCount,
                posts = res
            )
        }
    }
}