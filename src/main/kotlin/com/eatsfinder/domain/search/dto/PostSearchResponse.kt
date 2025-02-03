package com.eatsfinder.domain.search.dto

import com.eatsfinder.domain.post.model.Post
import java.time.LocalDateTime

data class PostSearchResponse(
    val userId: Long?,
    val postId: Long?,
    val placeName: String,
    val postThumbnailUrl: String,
    val isPostLike: Boolean,
    val postLikeCount: Int,
    val profileImage: String?,
    val nickname: String,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(post: Post, isPostLike: Boolean): PostSearchResponse {
            return PostSearchResponse(
                userId = post.userId.id,
                postId = post.id,
                placeName = post.placeId.name,
                postThumbnailUrl = post.thumbnailUrl,
                isPostLike = isPostLike,
                postLikeCount = post.likeCount,
                profileImage = post.userId.profileImage,
                nickname = post.userId.nickname,
                updatedAt = post.updatedAt
            )
        }
    }
}