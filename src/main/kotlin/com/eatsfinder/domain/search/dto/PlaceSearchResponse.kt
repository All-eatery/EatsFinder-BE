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
        fun from(place: Place, posts: List<Post>, star: List<StarRating>, isBookmark: Boolean): PlaceSearchResponse {
            val firstPost = posts.firstOrNull()
            val firstStar = star.firstOrNull()

            return PlaceSearchResponse(
                postThumbnailUrl = firstPost?.thumbnailUrl ?: "",
                placeName = place.name,
                roadAddress = place.roadAddress,
                starRating = firstStar?.star ?: 0,
                category = place.categoryId.name,
                isBookmark = isBookmark,
                updatedAt = place.updatedAt,
                likeCount = firstPost?.likeCount ?: 0
            )
        }
    }
}
