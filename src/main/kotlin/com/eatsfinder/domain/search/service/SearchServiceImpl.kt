package com.eatsfinder.domain.search.service

import com.eatsfinder.domain.bookmark.repository.BookmarkPlacesRepository
import com.eatsfinder.domain.follow.repository.FollowRepository
import com.eatsfinder.domain.search.model.KeywordLog
import com.eatsfinder.domain.search.repository.KeywordRepository
import com.eatsfinder.domain.like.dto.PaginationPostLikeResponse
import com.eatsfinder.domain.like.dto.PostLikeResponse
import com.eatsfinder.domain.like.model.PostLikes
import com.eatsfinder.domain.like.repository.PostLikeRepository
import com.eatsfinder.domain.place.repository.PlaceMenusRepository
import com.eatsfinder.domain.place.repository.PlaceRepository
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
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration

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
    private val bookmarkPlacesRepository: BookmarkPlacesRepository,
    private val redisTemplate: RedisTemplate<String, String>,
    private val keywordRepository: KeywordRepository,
) : SearchService {

    @Transactional
    override fun getSearchKeyword(
        keyword: String,
        searchFilter: SearchFilter?,
        postCursorId: Long?,
        placeCursorId: Long?,
        neighborCursorId: Long?,
        pageSize: Int
    ): SearchResponse {
        val objectMapper = ObjectMapper()

        // 캐시에서 가져오기
        val RedisCache = redisTemplate.opsForValue().get("keyword::$keyword")
        if (RedisCache != null) {
            try {
                val RedisCacheRes = objectMapper.readValue(RedisCache, SearchResponse::class.java)
                println("캐시에서 조회 성공: $RedisCacheRes")
                return RedisCacheRes
            } catch (e: Exception) {
                println("캐시 역직렬화 실패: ${e.message}")
            }
        }

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

        // 실제 검색 로직
        val response: SearchResponse = when (searchFilter) {
            SearchFilter.Places -> {
                val placeResult = searchPlaces(keyword, bookmark, placeCursorId, pageSize)
                SearchResponse(
                    pagination = placeResult.pagination,
                    posts = emptyList(),
                    places = placeResult.items,
                    neighbors = emptyList(),
                    postLastItemId = null,
                    placeLastItemId = placeResult.lastItemId,
                    neighborLastItemId = null
                )
            }

            SearchFilter.Posts -> {
                val postResult = searchPosts(keyword, user, postLike, postCursorId, pageSize)
                SearchResponse(
                    pagination = postResult.pagination,
                    posts = postResult.items,
                    places = emptyList(),
                    neighbors = emptyList(),
                    postLastItemId = postResult.lastItemId,
                    placeLastItemId = null,
                    neighborLastItemId = null
                )
            }

            SearchFilter.Neighbors -> {
                val userResult = searchUsers(keyword, userPostCounts, follow, neighborCursorId, pageSize)
                SearchResponse(
                    pagination = userResult.pagination,
                    posts = emptyList(),
                    places = emptyList(),
                    neighbors = userResult.items,
                    postLastItemId = null,
                    placeLastItemId = null,
                    neighborLastItemId = userResult.lastItemId
                )
            }

            SearchFilter.All -> searchAll(
                keyword, bookmark, postLike, user, userPostCounts, follow,
                postCursorId, placeCursorId, neighborCursorId, pageSize
            )

            else -> SearchResponse(null, emptyList(), emptyList(), emptyList(), null, null, null)
        }

        try {
            val responseJson = objectMapper.writeValueAsString(response)
            redisTemplate.opsForValue().set("keyword::$keyword", responseJson, Duration.ofMinutes(30))
        } catch (e: Exception) {
            println("Redis 캐시 저장 실패: ${e.message}")
        }

        // 검색어 로그 저장
        saveKeywordLog(keyword)

        return response
    }

    private inline fun <reified T> cache(
        keyword: String,
        supplier: () -> T
    ): T {
        val objectMapper = ObjectMapper()

        // 캐시에서 가져오기
        val redisCache = redisTemplate.opsForValue().get("keyword::$keyword")
        if (redisCache != null) {
            try {
                val cached = objectMapper.readValue(redisCache, T::class.java)
                println("캐시에서 조회 성공: $cached")
                return cached
            } catch (e: Exception) {
                println("캐시 역직렬화 실패: ${e.message}")
            }
        }

        val response = supplier()

        try {
            val responseJson = objectMapper.writeValueAsString(response)
            redisTemplate.opsForValue()
                .set("keyword::$keyword", responseJson, Duration.ofMinutes(30))
            println("캐시에 저장 성공: $keyword")
        } catch (e: Exception) {
            println("Redis 캐시 저장 실패: ${e.message}")
        }

        saveKeywordLog(keyword)

        return response
    }

    override fun getPostSearchKeyword(
        keyword: String,
        postCursorId: Long?,
        pageSize: Int
    ): PostSearchPaginationResponse {

        val userPrincipal = SecurityContextHolder.getContext().authentication?.principal as? UserPrincipal
        val user = userPrincipal?.let { userRepository.findByIdAndDeletedAt(it.id, null) }

        val postLike = user?.let { postLikeRepository.findByUserId(it) } ?: emptyList()

        return cache<PostSearchPaginationResponse>(keyword) {
            searchPosts(keyword, user, postLike, postCursorId, pageSize)
        }
    }

    override fun getPlaceSearchKeyword(
        keyword: String,
        placeCursorId: Long?,
        pageSize: Int
    ): PlaceSearchPaginationResponse {
        val userPrincipal = SecurityContextHolder.getContext().authentication?.principal as? UserPrincipal
        val user = userPrincipal?.let { userRepository.findByIdAndDeletedAt(it.id, null) }
        val bookmark = user?.let {
            bookmarkPlacesRepository.findByBookmarkIdUserId(it.id!!).mapNotNull { bookmarkPlace ->
                bookmarkPlace.placeId.id
            }.toSet()
        } ?: emptySet()

        return cache<PlaceSearchPaginationResponse>(keyword) {
            searchPlaces(keyword, bookmark, placeCursorId, pageSize)
        }
    }

    override fun getNeighborSearchKeyword(
        keyword: String,
        neighborCursorId: Long?,
        pageSize: Int
    ): NeighborSearchPaginationResponse {
        val userPrincipal = SecurityContextHolder.getContext().authentication?.principal as? UserPrincipal
        val user = userPrincipal?.let { userRepository.findByIdAndDeletedAt(it.id, null) }
        val users = userRepository.findAll().filter { it.deletedAt == null }

        val userPostCounts = users.associateWith { postRepository.findByUserId(it)?.size ?: 0 }
        val follow =
            user?.let { followRepository.findByFollowedUserId(it).mapNotNull { it.followingUserId.id }.toSet() }
                ?: emptySet()

        return cache<NeighborSearchPaginationResponse>(keyword) {
            searchUsers(keyword, userPostCounts, follow, neighborCursorId, pageSize)
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

        val totalCount = postLikeRepository.countLikesExcludingReportedPosts(user)
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

        val isLastPage = (searchPlace.items.size < pageSize) &&
                (searchUser.items.size < pageSize) &&
                (searchPost.items.size < pageSize)

        val pagination = PaginationItemsResponse(
            totalItems = totalCount,
            itemsPerPage = pageSize,
            totalPage = (totalCount / pageSize) + if (totalCount % pageSize > 0) 1 else 0,
            currentPage = 1,
            isLastPage = isLastPage
        )

        return SearchResponse(
            pagination = pagination,
            posts = searchPost.items,
            places = searchPlace.items,
            neighbors = searchUser.items,
            postLastItemId = searchPost.lastItemId,
            placeLastItemId = searchPlace.lastItemId,
            neighborLastItemId = searchUser.lastItemId
        )
    }

    private fun searchPlaces(
        keyword: String,
        bookmark: Set<Long>,
        placeCursorId: Long?,
        pageSize: Int
    ): PlaceSearchPaginationResponse {

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
            place.deletedAt == null &&
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

        return PlaceSearchPaginationResponse(
            pagination = pagination,
            items = filteredPlaces.take(pageSize),
            lastItemId = nextCursorId
        )
    }

    private fun searchPosts(
        keyword: String,
        user: User?,
        postLike: List<PostLikes>,
        postCursorId: Long?,
        pageSize: Int
    ): PostSearchPaginationResponse {

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
            post.deletedAt == null &&
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

        return PostSearchPaginationResponse(
            pagination = pagination,
            items = filteredPosts.take(pageSize),
            lastItemId = nextCursorId
        )
    }

    private fun searchUsers(
        keyword: String,
        userPostCounts: Map<User, Int>,
        follow: Set<Long>,
        neighborCursorId: Long?,
        pageSize: Int
    ): NeighborSearchPaginationResponse {

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
            user.deletedAt == null &&
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


        return NeighborSearchPaginationResponse(
            pagination = pagination,
            items = filteredUsers.take(pageSize),
            lastItemId = nextCursorId
        )
    }

    private fun saveKeywordLog(keyword: String): List<String> {
        // DB 업데이트
        keywordRepository.findByKeyword(keyword)?.let {
            it.count += 1
            keywordRepository.save(it)
            it.count
        } ?: run {
            val newKeyword = KeywordLog(keyword = keyword, count = 1.0)
            keywordRepository.save(newKeyword)
            newKeyword.count
        }

        try {
            // Redis에 키워드 문자열만 저장
            redisTemplate.opsForZSet().incrementScore("ranking", keyword, 1.0)
        } catch (e: Exception) {
            println("Redis ZSet 저장 오류: ${e.message}")
        }

        return findKeywordLog()
    }

    override fun findKeywordLog(): List<String> {
        val zSet = redisTemplate.opsForZSet()

        return zSet.reverseRange("ranking", 0, 6)?.toList() ?: emptyList()
    }
}