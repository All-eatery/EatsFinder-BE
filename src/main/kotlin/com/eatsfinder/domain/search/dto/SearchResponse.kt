package com.eatsfinder.domain.search.dto

import com.eatsfinder.domain.place.model.Place
import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.domain.starRating.model.StarRating
import com.eatsfinder.domain.user.model.User

data class SearchResponse(
    val posts: List<PostSearchResponse>?,
    val places: List<PlaceSearchResponse>?,
    val neighbors: List<NeighborPostResponse>?
){
    companion object {
        fun from(
            posts: List<Post>,
            places: List<Place>,
            users: List<User>,
            post: List<Post>,
            star: List<StarRating>,
            isPostLike: Boolean,
            isBookmark: Boolean,
            postCount: Int,
            isFollow: Boolean
        ): SearchResponse {
            return SearchResponse(
                posts = posts.map { PostSearchResponse.from(it, isPostLike) },
                places = places.map { PlaceSearchResponse.from(it, post, star, isBookmark) },
                neighbors = users.map { NeighborPostResponse.from(it, postCount, isFollow) }
            )
        }
    }
}