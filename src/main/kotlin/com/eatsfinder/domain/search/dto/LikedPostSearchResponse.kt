package com.eatsfinder.domain.search.dto

import com.eatsfinder.domain.post.model.Post
import java.time.LocalDateTime

data class LikedPostSearchResponse(
    val userId: Long?,
    val userImageUrl: String?,
    val placeName: String,
    val postId: Long?,
    val postThumbnailUrl: String,
    val postLikeCount: Int,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(post: Post): LikedPostSearchResponse {
            return LikedPostSearchResponse(
                userId = post.userId.id,
                userImageUrl = post.userId.profileImage,
                placeName = post.placeId.name ,
                postId = post.id,
                postThumbnailUrl = post.thumbnailUrl,
                postLikeCount = post.likeCount,
                updatedAt = post.updatedAt
            )
        }
    }
}

data class LikedPostsResponse(
    val likedPosts: List<LikedPostSearchResponse>
) {
    companion object {
        fun from(posts: List<Post>): LikedPostsResponse {
            val likedPostResponses = posts.map { LikedPostSearchResponse.from(it) }
            return LikedPostsResponse(likedPosts = likedPostResponses)
        }
    }
}