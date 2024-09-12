package com.eatsfinder.domain.user.dto.user

import com.eatsfinder.domain.post.model.Post
import java.time.format.DateTimeFormatter


data class OtherPeopleFeedResponse(
    val thumbnailUrl: String,
    val placeName: String,
    val content: String?,
    val createdAt: String
) {
    companion object {
        fun from(post: Post): OtherPeopleFeedResponse {
            val createdDate = DateTimeFormatter.ofPattern("yyyy.MM.dd")
            return OtherPeopleFeedResponse(
                thumbnailUrl = post.thumbnailUrl,
                placeName = post.placeId.name,
                content = post.content,
                createdAt = post.createdAt.toLocalDate().format(createdDate)
            )
        }
    }
}
