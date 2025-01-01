package com.eatsfinder.domain.search.service

import com.eatsfinder.domain.bookmark.repository.BookmarkRepository
import com.eatsfinder.domain.follow.repository.FollowRepository
import com.eatsfinder.domain.like.model.PostLikes
import com.eatsfinder.domain.like.repository.PostLikeRepository
import com.eatsfinder.domain.place.model.Place
import com.eatsfinder.domain.place.repository.PlaceMenusRepository
import com.eatsfinder.domain.place.repository.PlaceRepository
import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.domain.post.repository.PostRepository
import com.eatsfinder.domain.search.dto.NeighborPostResponse
import com.eatsfinder.domain.search.dto.PlaceSearchResponse
import com.eatsfinder.domain.search.dto.PostSearchResponse
import com.eatsfinder.domain.search.dto.SearchResponse
import com.eatsfinder.domain.search.model.SearchFilter
import com.eatsfinder.domain.starRating.repository.StarRatingRepository
import com.eatsfinder.domain.user.model.User
import com.eatsfinder.domain.user.repository.UserRepository
import com.eatsfinder.global.security.jwt.UserPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class SearchServiceImpl(
    private val postRepository: PostRepository,
    private val placeRepository: PlaceRepository,
    private val userRepository: UserRepository,
    private val postLikeRepository: PostLikeRepository,
    private val starRatingRepository: StarRatingRepository,
    private val followRepository: FollowRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val placeMenusRepository: PlaceMenusRepository

) : SearchService {
    override fun getSearchKeyword(keyword: String, searchFilter: SearchFilter?): SearchResponse {
        val userPrincipal = SecurityContextHolder.getContext().authentication?.principal as? UserPrincipal
        val user = userRepository.findByIdAndDeletedAt(userPrincipal?.id!!, null) ?: TODO()

        val places = placeRepository.findByDeletedAt(null) ?: emptyList()
        val posts = postRepository.findByDeletedAt(null) ?: emptyList()
        val users = userRepository.findAll().filter { it.deletedAt == null }
        val postLike = postLikeRepository.findByUserId(user)
        val postCount = postRepository.findByUserId(user)?.size ?: 0
        val follow = followRepository.findByFollowedUserId(user).mapNotNull { it.followingUserId.id }.toSet()

        val isBookmark = bookmarkRepository.findAll().any { it.userId.id == userPrincipal.id && it.userId.deletedAt == null }

        return when (searchFilter) {
            SearchFilter.PLACES -> searchPlaces(keyword, places, isBookmark)
            SearchFilter.POSTS -> searchPosts(keyword, posts, user, postLike)
            SearchFilter.USERS -> searchUsers(keyword, users, postCount, follow)
            else -> SearchResponse(emptyList(), emptyList(), emptyList())
        }
    }

    private fun searchPlaces(keyword: String, places: List<Place>, isBookmark: Boolean): SearchResponse {
        val filteredPlaces = places.filter { place ->
            place.name.contains(keyword, ignoreCase = true) ||
            placeMenusRepository.findByPlaceIdAndMenu(place, keyword)?.menu?.contains(keyword, ignoreCase = true) == true ||
            place.address.contains(keyword, ignoreCase = true) ||
            place.categoryId.name.contains(keyword, ignoreCase = true)
        }.map { place ->
            PlaceSearchResponse.from(
                post = postRepository.findByPlaceId(place)!!,
                place = place,
                star = starRatingRepository.findByPlaceId(place)!!,
                isBookmark = isBookmark
            )
        }

        return SearchResponse(post = emptyList(), place = filteredPlaces, neighbor = emptyList())
    }

    private fun searchPosts(keyword: String, posts: List<Post>, user: User, postLike: List<PostLikes>): SearchResponse {
        val filteredPosts = posts.filter { post ->
            post.placeId.name.contains(keyword, ignoreCase = true) ||
            placeMenusRepository.findByPlaceIdAndMenu(post.placeId, keyword)?.menu?.contains(keyword, ignoreCase = true) == true ||
            post.placeId.address.contains(keyword, ignoreCase = true) ||
            post.userId.nickname.contains(keyword, ignoreCase = true) ||
            (post.content?.let { it.contains(keyword, ignoreCase = true) } == true) ||
            post.keywordTag.contains(keyword, ignoreCase = true) ||
            post.placeId.categoryId.name.contains(keyword, ignoreCase = true)
        }.map { post ->
            PostSearchResponse.from(
                post,
                isPostLike = postLike.any { like -> like.postId.id == post.id && like.userId.id == user.id }
            )
        }

        return SearchResponse(post = filteredPosts, place = emptyList(), neighbor = emptyList())
    }

    private fun searchUsers(keyword: String, users: List<User>, postCount: Int, follow: Set<Long>): SearchResponse {
        val filteredUsers = users.filter { user ->
            user.nickname.contains(keyword, ignoreCase = true)
        }.map { user ->
            val isFollow = follow.contains(user.id)
            NeighborPostResponse.from(user, postCount, isFollow)
        }

        return SearchResponse(post = emptyList(), place = emptyList(), neighbor = filteredUsers)
    }
}