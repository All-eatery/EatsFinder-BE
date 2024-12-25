package com.eatsfinder.domain.search.service

import com.eatsfinder.domain.bookmark.repository.BookmarkRepository
import com.eatsfinder.domain.follow.repository.FollowRepository
import com.eatsfinder.domain.like.repository.PostLikeRepository
import com.eatsfinder.domain.place.repository.PlaceMenusRepository
import com.eatsfinder.domain.place.repository.PlaceRepository
import com.eatsfinder.domain.post.repository.PostRepository
import com.eatsfinder.domain.search.dto.NeighborPostResponse
import com.eatsfinder.domain.search.dto.PlaceSearchResponse
import com.eatsfinder.domain.search.dto.PostSearchResponse
import com.eatsfinder.domain.search.dto.SearchResponse
import com.eatsfinder.domain.search.model.SearchFilter
import com.eatsfinder.domain.starRating.repository.StarRatingRepository
import com.eatsfinder.domain.user.model.User
import com.eatsfinder.domain.user.repository.UserRepository
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
    override fun getSearchKeyword(userId: Long?, keyword: String, searchFilter: SearchFilter?): SearchResponse {
        val user = userRepository.findByIdAndDeletedAt(userId!!, null) ?: TODO()
        val places = placeRepository.findByDeletedAt(null) ?: emptyList()
        val posts = postRepository.findByDeletedAt(null) ?: emptyList()
        val users = userRepository.findAll().filter { it.deletedAt == null }
        val postLike = postLikeRepository.findByUserId(user)
        val postCount = postRepository.findByUserId(user)?.size ?: 0
        val follow = followRepository.findByFollowedUserId(user).map {
            it.followingUserId.id
        }.toSet()

        val isBookmark = bookmarkRepository.findAll().any {
            it.userId.id == userId && it.userId.deletedAt == null
        }



        return when (searchFilter) {
            SearchFilter.PLACES -> {
                SearchResponse(
                    post = emptyList(),
                    place = places.filter { placeId ->
                        val menu = placeMenusRepository.findByPlaceIdAndMenu(placeId, keyword)
                        placeId.name.contains(keyword, ignoreCase = true) ||
                        menu?.menu?.contains(keyword, ignoreCase = true) == true ||
                        placeId.address.contains(keyword, ignoreCase = true) ||
                        placeId.categoryId.name.contains(keyword, ignoreCase = true)
                    }.map { place ->
                        PlaceSearchResponse.from(
                            post = postRepository.findByPlaceId(place)!!,
                            place = place,
                            star = starRatingRepository.findByPlaceId(place)!!,
                            isBookmark = isBookmark
                        )
                    },
                    neighbor = emptyList()
                )
            }

            SearchFilter.POSTS -> {
                SearchResponse(
                    post = posts.filter { postId ->
                        val menu = placeMenusRepository.findByPlaceIdAndMenu(postId.placeId, keyword)
                        postId.placeId.name.contains(keyword, ignoreCase = true) ||
                        menu?.menu?.contains(keyword, ignoreCase = true) == true ||
                        postId.placeId.address.contains(keyword, ignoreCase = true) ||
                        postId.userId.nickname.contains(keyword, ignoreCase = true) ||
                        (postId.content?.let { it.contains(keyword, ignoreCase = true) } == true)||
                        postId.keywordTag.contains(keyword, ignoreCase = true) ||
                        postId.placeId.categoryId.name.contains(keyword, ignoreCase = true)
                    }.map { post ->
                        PostSearchResponse.from(
                            post,
                            isPostLike = postLike.any { like -> like.postId.id == post.id && like.userId.id == user.id }
                        )
                    },
                    place = emptyList(),
                    neighbor = emptyList()
                )
            }

            SearchFilter.USERS -> {
                SearchResponse(
                    post = emptyList(),
                    place = emptyList(),
                    neighbor = users.filter { user1 ->
                        user1.nickname.contains(keyword, ignoreCase = true)
                    }.map { user1 ->
                        val isFollow = follow.contains(user1.id)
                        NeighborPostResponse.from(user1, postCount, isFollow) }

                    // 일단은 닉네임만 조회 가능하도록 해둠.
                )
            }



            else -> {
                SearchResponse(emptyList(), emptyList(), emptyList())
            }
        }
    }
}