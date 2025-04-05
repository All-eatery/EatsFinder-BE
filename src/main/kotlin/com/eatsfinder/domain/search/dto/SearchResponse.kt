package com.eatsfinder.domain.search.dto

import com.eatsfinder.domain.place.model.Place
import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.domain.starRating.model.StarRating
import com.eatsfinder.domain.user.model.User
import com.eatsfinder.global.pagination.PaginationItemsResponse

data class SearchResponse(
    val pagination: PaginationItemsResponse?,
    val posts: List<PostSearchResponse>?,
    val places: List<PlaceSearchResponse>?,
    val neighbors: List<NeighborPostResponse>?,
    val postLastItemId: Long?,
    val placeLastItemId: Long?,
    val neighborLastItemId: Long?
){
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