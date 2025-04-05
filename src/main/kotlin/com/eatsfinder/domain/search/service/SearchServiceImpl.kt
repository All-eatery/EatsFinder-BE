package com.eatsfinder.domain.search.service

import com.eatsfinder.domain.bookmark.repository.BookmarkPlacesRepository
import com.eatsfinder.domain.follow.repository.FollowRepository
import com.eatsfinder.domain.like.dto.PaginationPostLikeResponse
import com.eatsfinder.domain.like.dto.PostLikeResponse
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
import com.eatsfinder.global.pagination.PaginationItemsResponse
import com.eatsfinder.global.security.jwt.UserPrincipal
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
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
    override fun getSearchKeyword(
        keyword: String,
        searchFilter: SearchFilter?,
        postCursorId: Long?,
        placeCursorId: Long?,
        neighborCursorId: Long?,
        pageSize: Int
    ): SearchResponse {
        val userPrincipal = SecurityContextHolder.getContext().authentication?.principal as? UserPrincipal
        val user = userPrincipal?.let { userRepository.findByIdAndDeletedAt(it.id, null) }
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
            SearchFilter.Places -> searchPlaces(keyword, bookmark, placeCursorId, pageSize)
            SearchFilter.Posts -> searchPosts(keyword, user, postLike, postCursorId, pageSize)
            SearchFilter.Neighbors -> searchUsers(keyword, userPostCounts, follow, neighborCursorId, pageSize)
            SearchFilter.All -> searchAll(
                keyword,
                bookmark,
                postLike,
                user,
                userPostCounts,
                follow,
                postCursorId,
                placeCursorId,
                neighborCursorId,
                pageSize
            )

            else -> SearchResponse(null, emptyList(), emptyList(), emptyList(), null, null, null)
        }
    }

    override fun getLikePostSearchKeyword(
        cursorId: Long?,
        pageSize: Int,
        keyword: String,
        userId: Long
    ): PaginationPostLikeResponse {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException(
            "user",
            "이 유저 아이디(${userId})는 존재하지 않습니다."
        )

        val pageable: Pageable = PageRequest.of(0, pageSize + 1)


        val likedPosts = if (cursorId == null) {
            postLikeRepository.findAllByUserId(user, pageable)
        } else {
            postLikeRepository.findAllByUserIdAndIdGreaterThan(user, cursorId, pageable)
        }.filterNot {
            reportPostRepository.existsByPostIdAndUserId(it.postId, user)
        }

        val filteredLikedPosts = likedPosts.filter { likedPost ->
            likedPost.postId.placeId.name.contains(
                keyword,
                ignoreCase = true
            ) || likedPost.postId.userId.nickname.contains(keyword, ignoreCase = true)
        }


        val postLikeList: List<PostLikeResponse> = filteredLikedPosts.map { like ->
            PostLikeResponse(
                id = like.id,
                postId = like.postId.id,
                postPlaceName = like.postId.placeId.name,
                postThumbnailUrl = like.postId.thumbnailUrl,
                isPostLike = (like.userId.id == user.id),
                postUserNickname = like.postId.userId.nickname,
                postUserProfileImage = like.postId.userId.profileImage
            )
        }
        val isLastPage = postLikeList.size <= pageSize

        val nextCursorId = when {
            postLikeList.isEmpty() -> null
            postLikeList.size > pageSize -> postLikeList[pageSize - 1].id
            else -> postLikeList.last().id
        }

        val totalCount = postLikeRepository.countByUserId(user)
        val pagination = PaginationItemsResponse(
            totalItems = totalCount,
            itemsPerPage = pageSize,
            totalPage = (filteredLikedPosts.size / pageSize).toLong() + if (filteredLikedPosts.size % pageSize > 0) 1 else 0,
            currentPage = 1,
            isLastPage = isLastPage
        )

        if (postLikeList.isEmpty()) {
            return PaginationPostLikeResponse(
                pagination = PaginationItemsResponse(
                    totalItems = 0,
                    itemsPerPage = pageSize,
                    totalPage = 0,
                    currentPage = 1,
                    isLastPage = true
                ),
                items = emptyList(),
                lastItemId = 0
            )
        }

        return PaginationPostLikeResponse(
            pagination = pagination,
            items = postLikeList.take(pageSize),
            lastItemId = nextCursorId
        )

    }

    private fun searchAll(
        keyword: String,
        bookmark: Set<Long>,
        postLike: List<PostLikes>,
        user: User?,
        userPostCounts: Map<User, Int>,
        follow: Set<Long>,
        postCursorId: Long?,
        placeCursorId: Long?,
        neighborCursorId: Long?,
        pageSize: Int
    ): SearchResponse {
        val searchPlace = searchPlaces(keyword, bookmark, placeCursorId, pageSize)
        val searchPost = searchPosts(keyword, user, postLike, postCursorId, pageSize)
        val searchUser = searchUsers(keyword, userPostCounts, follow, neighborCursorId, pageSize)

        val totalCount = postRepository.countTotalByKeyword(keyword) +
                placeRepository.countTotalByKeyword(keyword) +
                userRepository.countByNicknameContaining(keyword)

        val isLastPage = (searchPlace.places?.size ?: 0) < pageSize &&
                (searchUser.neighbors?.size ?: 0) < pageSize &&
                (searchPost.posts?.size ?: 0) < pageSize

        val pagination = PaginationItemsResponse(
            totalItems = totalCount,
            itemsPerPage = pageSize,
            totalPage = (totalCount / pageSize) + if (totalCount % pageSize > 0) 1 else 0,
            currentPage = 1,
            isLastPage = isLastPage
        )

        val postLastItemId = searchPost.postLastItemId
        val placeLastItemId = searchPlace.placeLastItemId
        val neighborLastItemId = searchUser.neighborLastItemId

        return SearchResponse(
            pagination = pagination,
            posts = searchPost.posts,
            places = searchPlace.places,
            neighbors = searchUser.neighbors,
            postLastItemId = postLastItemId,
            placeLastItemId = placeLastItemId,
            neighborLastItemId = neighborLastItemId
        )
    }

    private fun searchPlaces(
        keyword: String,
        bookmark: Set<Long>,
        placeCursorId: Long?,
        pageSize: Int
    ): SearchResponse {

        val pageable: Pageable = PageRequest.of(0, pageSize + 1)

        val paginationPlace = if (placeCursorId == null) {
            placeRepository.findAllByKeywords(keyword, pageable)
        } else {
            placeRepository.findAllByKeywordsAndIdGreaterThan(
                keyword,
                placeCursorId,
                pageable
            )
        }

        val filteredPlaces = paginationPlace.filter { place ->
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

        val searchPlace = when (placeCursorId) {
            null -> filteredPlaces.take(pageSize)
            else -> filteredPlaces.filter { it.placeId > placeCursorId }.take(pageSize)
        }

        val isLastPage = paginationPlace.size <= pageSize

        val nextCursorId = when {
            searchPlace.isEmpty() -> null
            searchPlace.size > pageSize -> filteredPlaces[pageSize - 1].placeId
            else -> searchPlace.last().placeId
        }

        val totalCount = placeRepository.countTotalByKeyword(keyword)
        val pagination = PaginationItemsResponse(
            totalItems = totalCount,
            itemsPerPage = pageSize,
            totalPage = (filteredPlaces.size / pageSize).toLong() + if (filteredPlaces.size % pageSize > 0) 1 else 0,
            currentPage = 1,
            isLastPage = isLastPage
        )

        return SearchResponse(
            posts = emptyList(),
            places = filteredPlaces.take(pageSize),
            neighbors = emptyList(),
            pagination = pagination,
            postLastItemId = null,
            placeLastItemId = nextCursorId,
            neighborLastItemId = null
        )
    }

    private fun searchPosts(
        keyword: String,
        user: User?,
        postLike: List<PostLikes>,
        postCursorId: Long?,
        pageSize: Int
    ): SearchResponse {

        val pageable: Pageable = PageRequest.of(0, pageSize + 1)
        val paginationPost = if (postCursorId == null) {
            postRepository.findAllByKeywords(keyword, pageable)
        } else {
            postRepository.findAllByKeywordsAndIdGreaterThan(
                keyword,
                postCursorId,
                pageable
            )
        }

        val filteredPosts = paginationPost.filter { post ->
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

        val searchPost = when (postCursorId) {
            null -> filteredPosts.take(pageSize)
            else -> filteredPosts.filter { it.postId!! > postCursorId }.take(pageSize)
        }

        val isLastPage = paginationPost.size <= pageSize

        val nextCursorId = when {
            searchPost.isEmpty() -> null
            searchPost.size > pageSize -> filteredPosts[pageSize - 1].postId
            else -> searchPost.last().postId
        }

        val totalCount = postRepository.countTotalByKeyword(keyword)
        val pagination = PaginationItemsResponse(
            totalItems = totalCount,
            itemsPerPage = pageSize,
            totalPage = (filteredPosts.size / pageSize).toLong() + if (filteredPosts.size % pageSize > 0) 1 else 0,
            currentPage = 1,
            isLastPage = isLastPage
        )

        return SearchResponse(
            posts = filteredPosts.take(pageSize),
            places = emptyList(),
            neighbors = emptyList(),
            pagination = pagination,
            postLastItemId = nextCursorId,
            placeLastItemId = null,
            neighborLastItemId = null
        )
    }

    private fun searchUsers(
        keyword: String,
        userPostCounts: Map<User, Int>,
        follow: Set<Long>,
        neighborCursorId: Long?,
        pageSize: Int
    ): SearchResponse {

        val pageable: Pageable = PageRequest.of(0, pageSize + 1)
        val paginationUser = if (neighborCursorId == null) {
            userRepository.findAllByNicknameContaining(keyword, pageable)
        } else {
            userRepository.findAllByKeywordsAndIdGreaterThan(
                keyword,
                neighborCursorId,
                pageable
            )
        }

        val filteredUsers = paginationUser.filter { user ->
            user.nickname.contains(keyword, ignoreCase = true)
        }.map { user ->
            val isFollow = follow.contains(user.id)
            val postCount = userPostCounts[user] ?: 0
            NeighborPostResponse.from(user, postCount, isFollow)
        }

        val searchNeighbor = when (neighborCursorId) {
            null -> filteredUsers.take(pageSize)
            else -> filteredUsers.filter { it.neighbor.id > neighborCursorId }.take(pageSize)
        }

        val isLastPage = paginationUser.size <= pageSize

        val nextCursorId = if (searchNeighbor.isNotEmpty()) {
            searchNeighbor.last().neighbor.id
        } else {
            null
        }

        val totalCount = userRepository.countByNicknameContaining(keyword)

        val pagination = PaginationItemsResponse(
            totalItems = totalCount,
            itemsPerPage = pageSize,
            totalPage = (filteredUsers.size / pageSize).toLong() + if (filteredUsers.size % pageSize > 0) 1 else 0,
            currentPage = 1,
            isLastPage = isLastPage
        )

        return SearchResponse(
            posts = emptyList(),
            places = emptyList(),
            neighbors = filteredUsers.take(pageSize),
            pagination = pagination,
            postLastItemId = null,
            placeLastItemId = null,
            neighborLastItemId = nextCursorId
        )
    }
}