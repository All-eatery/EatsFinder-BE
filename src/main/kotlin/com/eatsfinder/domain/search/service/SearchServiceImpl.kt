package com.eatsfinder.domain.search.service

import com.eatsfinder.domain.bookmark.repository.BookmarkPlacesRepository
import com.eatsfinder.domain.follow.repository.FollowRepository
import com.eatsfinder.domain.like.dto.PostLikesResponse
import com.eatsfinder.domain.like.model.PostLikes
import com.eatsfinder.domain.like.repository.PostLikeRepository
import com.eatsfinder.domain.place.model.Place
import com.eatsfinder.domain.place.repository.PlaceMenusRepository
import com.eatsfinder.domain.place.repository.PlaceRepository
import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.domain.post.repository.PostRepository
import com.eatsfinder.domain.report.repository.ReportPostRepository
import com.eatsfinder.domain.search.dto.*
import com.eatsfinder.domain.search.model.SearchFilter
import com.eatsfinder.domain.starRating.repository.StarRatingRepository
import com.eatsfinder.domain.user.model.User
import com.eatsfinder.domain.user.repository.UserRepository
import com.eatsfinder.global.exception.ModelNotFoundException
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
    private val placeMenusRepository: PlaceMenusRepository,
    private val reportPostRepository: ReportPostRepository,
    private val bookmarkPlacesRepository: BookmarkPlacesRepository

) : SearchService {
    override fun getSearchKeyword(keyword: String, searchFilter: SearchFilter?): SearchResponse {
        val userPrincipal = SecurityContextHolder.getContext().authentication?.principal as? UserPrincipal
        val user = userPrincipal?.let { userRepository.findByIdAndDeletedAt(it.id, null) }

        val places = placeRepository.findByDeletedAt(null) ?: emptyList()
        val posts = postRepository.findByDeletedAt(null) ?: emptyList()
        val users = userRepository.findAll().filter { it.deletedAt == null }

        val postLike = user?.let { postLikeRepository.findByUserId(it) } ?: emptyList()
        val userPostCounts = users.associateWith { postRepository.findByUserId(it)?.size ?: 0 }
        val follow =
            user?.let { followRepository.findByFollowedUserId(it).mapNotNull { it.followingUserId.id }.toSet() }
                ?: emptySet()


        val bookmark = user?.let {
            bookmarkPlacesRepository.findByBookmarkIdUserId(it.id!!).mapNotNull { bookmarkPlace ->
                bookmarkPlace.placeId.id
            }.toSet()
        } ?: emptySet()


        return when (searchFilter) {
            SearchFilter.PLACES -> searchPlaces(keyword, places, bookmark)
            SearchFilter.POSTS -> searchPosts(keyword, posts, user, postLike)
            SearchFilter.USERS -> searchUsers(keyword, users, userPostCounts, follow)
            SearchFilter.ALL -> searchAllThings(
                keyword,
                places,
                bookmark,
                posts,
                users,
                postLike,
                user,
                userPostCounts,
                follow
            )

            else -> SearchResponse(emptyList(), emptyList(), emptyList())
        }
    }

    override fun getLikePostSearchKeyword(keyword: String, userId: Long): PostLikesResponse {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException(
            "user",
            "이 유저 아이디(${userId})는 존재하지 않습니다."
        )

        val likedPosts = postLikeRepository.findByUserId(user).filterNot {
            reportPostRepository.existsByPostIdAndUserId(it.postId, user)
        }

        val filteredLikedPosts = likedPosts.filter { likedPost ->
            likedPost.postId.placeId.name.contains(
                keyword,
                ignoreCase = true
            ) || likedPost.postId.userId.nickname.contains(keyword, ignoreCase = true)
        }


        val postCount = filteredLikedPosts.size
        return PostLikesResponse.from(likedPosts, user, postCount)

    }

    private fun searchAllThings(
        keyword: String,
        places: List<Place>,
        bookmark: Set<Long>,
        posts: List<Post>,
        users: List<User>,
        postLike: List<PostLikes>,
        user: User?,
        userPostCounts: Map<User, Int>,
        follow: Set<Long>
    ): SearchResponse {
        val searchPlace = searchPlaces(keyword, places, bookmark)
        val searchPost = searchPosts(keyword, posts, user, postLike)
        val searchUser = searchUsers(keyword, users, userPostCounts, follow)

        return SearchResponse(
            post = searchPost.post,
            place = searchPlace.place,
            neighbor = searchUser.neighbor
        )
    }

    private fun searchPlaces(keyword: String, places: List<Place>, bookmark: Set<Long>): SearchResponse {
        val filteredPlaces = places.filter { place ->
            place.name.contains(keyword, ignoreCase = true) ||
                    placeMenusRepository.findByPlaceIdAndMenu(place, keyword)?.menu?.contains(
                        keyword,
                        ignoreCase = true
                    ) == true ||
                    place.address.contains(keyword, ignoreCase = true) ||
                    place.categoryId.name.contains(keyword, ignoreCase = true)
        }.map { place ->
            val posts = postRepository.findByPlaceId(place)
            val stars = starRatingRepository.findByPlaceId(place)
            val isBookmark = bookmark.contains(place.id)
            PlaceSearchResponse.from(
                posts = posts ?: emptyList(),
                place = place,
                star = stars ?: emptyList(),
                isBookmark = isBookmark
            )
        }

        return SearchResponse(post = emptyList(), place = filteredPlaces, neighbor = emptyList())
    }

    private fun searchPosts(
        keyword: String,
        posts: List<Post>,
        user: User?,
        postLike: List<PostLikes>
    ): SearchResponse {
        val filteredPosts = posts.filter { post ->
            !reportPostRepository.existsByPostIdAndUserId(post, user) &&
            post.placeId.name.contains(keyword, ignoreCase = true) ||
                    placeMenusRepository.findByPlaceIdAndMenu(post.placeId, keyword)?.menu?.contains(
                        keyword,
                        ignoreCase = true
                    ) == true ||
                    post.placeId.address.contains(keyword, ignoreCase = true) ||
                    post.userId.nickname.contains(keyword, ignoreCase = true) ||
                    (post.content?.let { it.contains(keyword, ignoreCase = true) } == true) ||
                    post.placeId.categoryId.name.contains(keyword, ignoreCase = true)
        }.map { post ->
            PostSearchResponse.from(
                post,
                isPostLike = postLike.any { like -> like.postId.id == post.id && like.userId.id == user?.id }
            )
        }

        return SearchResponse(post = filteredPosts, place = emptyList(), neighbor = emptyList())
    }

    private fun searchUsers(
        keyword: String,
        users: List<User>,
        userPostCounts: Map<User, Int>,
        follow: Set<Long>
    ): SearchResponse {
        val filteredUsers = users.filter { user ->
            user.nickname.contains(keyword, ignoreCase = true)
        }.map { user ->
            val isFollow = follow.contains(user.id)
            val postCount = userPostCounts[user] ?: 0
            NeighborPostResponse.from(user, postCount, isFollow)
        }

        return SearchResponse(post = emptyList(), place = emptyList(), neighbor = filteredUsers)
    }
}