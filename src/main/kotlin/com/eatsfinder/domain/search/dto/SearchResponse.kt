package com.eatsfinder.domain.search.dto

import com.eatsfinder.domain.place.model.Place
import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.domain.starRating.model.StarRating
import com.eatsfinder.domain.user.model.User
import com.eatsfinder.global.pagination.PaginationItemsResponse
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.io.Serializable

@JsonDeserialize
data class SearchResponse @JsonCreator constructor(
    @JsonProperty("pagination") val pagination: PaginationItemsResponse? = null,
    @JsonProperty("posts") val posts: List<PostSearchResponse>? = null,
    @JsonProperty("places") val places: List<PlaceSearchResponse>? = null,
    @JsonProperty("neighbors") val neighbors: List<NeighborPostResponse>? = null,
    @JsonProperty("postLastItemId") val postLastItemId: Long? = null,
    @JsonProperty("placeLastItemId") val placeLastItemId: Long? = null,
    @JsonProperty("neighborLastItemId") val neighborLastItemId: Long? = null
) : Serializable {
    companion object {
        fun from(
            posts: List<Post>,
            places: List<Place>,
            neighbors: List<User>,
            post: List<Post>,
            star: List<StarRating>,
            isPostLike: Boolean,
            isBookmark: Boolean,
            postCount: Int,
            isFollow: Boolean,
            pagination: PaginationItemsResponse?,
            postLastItemId: Long?,
            placeLastItemId: Long?,
            neighborLastItemId: Long?
        ): SearchResponse {
            return SearchResponse(
                pagination = pagination,
                posts = posts.map { PostSearchResponse.from(it, isPostLike) },
                places = places.map { PlaceSearchResponse.from(it, post, star, isBookmark) },
                neighbors = neighbors.map { NeighborPostResponse.from(it, postCount, isFollow) },
                postLastItemId = postLastItemId,
                placeLastItemId = placeLastItemId,
                neighborLastItemId = neighborLastItemId
            )
        }
    }
}