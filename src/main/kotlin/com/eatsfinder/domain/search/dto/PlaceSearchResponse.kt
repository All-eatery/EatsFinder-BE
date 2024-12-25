package com.eatsfinder.domain.search.dto

import com.eatsfinder.domain.place.model.Place
import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.domain.starRating.model.StarRating
import java.time.LocalDateTime

data class PlaceSearchResponse(
    val postThumbnailUrl: String,
    val placeName: String,
    val roadAddress: String,
    val starRating: Int,
    val category: String,
    val isBookmark: Boolean,
    val updatedAt: LocalDateTime,
    val likeCount: Int
) {
    companion object {
        fun from(place: Place, post: Post, star: StarRating, isBookmark: Boolean): PlaceSearchResponse {
            return PlaceSearchResponse(
                postThumbnailUrl = post.thumbnailUrl,
                placeName = place.name,
                roadAddress = place.roadAddress,
                starRating = star.star,
                category = place.categoryId.name,
                isBookmark = isBookmark,
                updatedAt = place.updatedAt,
                likeCount = post.likeCount
            )
        }
    }
}
