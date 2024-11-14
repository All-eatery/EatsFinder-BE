package com.eatsfinder.domain.search.dto

import java.time.LocalDateTime

data class PlaceSearchResponse(
    val placeThumbnailUrl: String,
    val placeName: String,
    val roadAddress: String,
    val starRating: String,
    val category: String,
    val isBookmark: Boolean,
    val updatedAt: LocalDateTime,
    val likeCount: Int
)
