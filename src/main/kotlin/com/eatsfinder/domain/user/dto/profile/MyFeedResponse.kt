package com.eatsfinder.domain.user.dto.profile

import com.eatsfinder.domain.post.model.Post
import java.time.LocalDateTime

data class MyFeedResponse(
    val thumbnailUrl: String,
    val placeName: String,
    val content: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(post: Post): MyFeedResponse {
            return MyFeedResponse(
                thumbnailUrl = post.thumbnailUrl,
                placeName = post.placeId.name,
                content = post.content,
                createdAt = post.createdAt
            )
        }
    }
}
