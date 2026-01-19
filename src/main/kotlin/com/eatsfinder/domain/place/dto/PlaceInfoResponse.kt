package com.eatsfinder.domain.place.dto

import com.eatsfinder.domain.place.model.Place
import com.eatsfinder.domain.post.model.Post

data class PlaceInfoResponse(
    val id: Long?,
    val name: String,
    val lng: Double,
    val lat: Double,
    val address: String,
    val roadAddress: String,
    val category: String,
    val postId: Long?,
    val rating: Float?,
    val postThumbnailUrl: String?,
) {
    companion object {
        fun from(
            places: List<Place>,
            postMap: Map<Long?, Post>?
        ): List<PlaceInfoResponse> {

            return places.map { place ->
                val post = postMap?.get(place.id)

                PlaceInfoResponse(
                    id = place.id,
                    name = place.name,
                    lng = place.x,
                    lat = place.y,
                    address = place.address,
                    roadAddress = place.roadAddress,
                    category = place.categoryId.name,
                    postId = post?.id,
                    rating = post?.ratingId?.star,
                    postThumbnailUrl = post?.thumbnailUrl
                )
            }
        }
    }
}
