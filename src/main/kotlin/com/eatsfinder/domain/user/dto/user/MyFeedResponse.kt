package com.eatsfinder.domain.user.dto.user

import com.eatsfinder.domain.post.model.Post
import java.time.format.DateTimeFormatter


data class MyFeedResponse(
    val thumbnailUrl: String,
    val placeName: String,
    val content: String?,
    val createdAt: String
) {
    companion object {
        fun from(post: Post): MyFeedResponse {
            val createdDate = DateTimeFormatter.ofPattern("yyyy.MM.dd")
            return MyFeedResponse(
                thumbnailUrl = post.thumbnailUrl,
                placeName = post.placeId.name,
                content = post.content,
                createdAt = post.createdAt.toLocalDate().format(createdDate)
            )
        }
    }
}
