package com.eatsfinder.domain.search.dto

import com.eatsfinder.domain.post.model.Post
import java.time.LocalDateTime

data class PostSearchResponse(
    val userId: Long?,
    val userImageUrl: String?,
    val placeName: String,
    val postId: Long?,
    val postThumbnailUrl: String,
    val isPostLike: Boolean,
    val postLikeCount: Int,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(post: Post, isPostLike: Boolean): PostSearchResponse {
            return PostSearchResponse(
                userId = post.userId.id,
                userImageUrl = post.userId.profileImage,
                placeName = post.placeId.name,
                postId = post.id,
                postThumbnailUrl = post.thumbnailUrl,
                isPostLike = isPostLike,
                postLikeCount = post.likeCount,
                updatedAt = post.updatedAt
            )
        }
    }
}