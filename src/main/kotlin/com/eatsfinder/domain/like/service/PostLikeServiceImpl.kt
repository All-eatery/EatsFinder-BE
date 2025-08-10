package com.eatsfinder.domain.like.service

import com.eatsfinder.domain.like.dto.PaginationPostLikeResponse
import com.eatsfinder.domain.like.dto.PostLikeResponse
import com.eatsfinder.domain.like.model.PostLikes
import com.eatsfinder.domain.like.repository.PostLikeRepository
import com.eatsfinder.domain.post.repository.PostRepository
import com.eatsfinder.domain.report.repository.ReportPostRepository
import com.eatsfinder.domain.user.model.MyActiveType
import com.eatsfinder.domain.user.model.UserLog
import com.eatsfinder.domain.user.repository.UserLogRepository
import com.eatsfinder.domain.user.repository.UserRepository
import com.eatsfinder.global.exception.ModelNotFoundException
import com.eatsfinder.global.exception.like.DefaultZeroException
import com.eatsfinder.global.exception.profile.MyProfileException
import com.eatsfinder.global.pagination.PaginationItemsResponse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostLikeServiceImpl(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val postLikeRepository: PostLikeRepository,
    private val userLogRepository: UserLogRepository,
    private val reportPostRepository: ReportPostRepository
) : PostLikeService {

    @Transactional
    override fun createPostLikes(userId: Long, postId: Long) {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException(
            "user",
            "이 유저 아이디(${userId})는 존재하지 않습니다."
        )
        val post = postRepository.findByIdAndDeletedAt(postId, null) ?: throw ModelNotFoundException(
            "post",
            "이 게시물 아이디: (${postId})는 존재하지 않습니다."
        )
        val postLike = postLikeRepository.findByUserIdAndPostId(user, post)

        if (post.userId.id == user.id) {
            throw MyProfileException("본인 게시물이므로 좋아요를 할 수 없습니다.")
        }

        if (post.likeCount < 0) throw DefaultZeroException("좋아요 수의 기본값은 0입니다.")

        if (postLike == null) {
            post.likeCount++
            postRepository.save(post)
            userLogRepository.save(
                UserLog(
                    userId = user,
                    postLikeId =  PostLikes(userId = user, postId = post),
                    commentLikeId = null,
                    commentId = null,
                    replyId = null,
                    replyLikeId = null,
                    myActiveType = MyActiveType.POST_LIKES
                )
            )
        } else {
            throw ModelNotFoundException("like", "좋아요(${postId})는 한번 밖에 하지 못합니다.")
        }
    }

    @Transactional
    override fun deletePostLikes(userId: Long, postId: Long) {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException(
            "user",
            "이 유저 아이디(${userId})는 존재하지 않습니다."
        )
        val post = postRepository.findByIdAndDeletedAt(postId, null) ?: throw ModelNotFoundException(
            "post",
            "이 게시물 아이디: (${postId})는 존재하지 않습니다."
        )
        val postLike = postLikeRepository.findByUserIdAndPostId(user, post)

        if (post.userId.id == user.id) {
            throw MyProfileException("본인 게시물이므로 좋아요를 취소할 수 없습니다.")
        }

        if (postLike != null) {
            if (post.likeCount > 0) {
                postLikeRepository.delete(postLike)
                post.likeCount--
                postRepository.save(post)

                val userLog =
                    userLogRepository.findUserLogByPostLikeIdAndMyActiveType(postLike, MyActiveType.POST_LIKES)
                        ?: throw ModelNotFoundException("userLog")
                userLogRepository.delete(userLog)
            } else {
                throw DefaultZeroException("좋아요 수의 기본값은 0입니다.")
            }
        } else {
            throw ModelNotFoundException("like", "좋아요(${postId})는 존재하지 않아 취소할 수 없습니다.")
        }
    }

    @Transactional(readOnly = true)
    override fun getPostLikes(userId: Long, cursorId: Long?, pageSize: Int): PaginationPostLikeResponse {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException(
            "user",
            "이 유저 아이디(${userId})는 존재하지 않습니다."
        )

        val pageable: Pageable = PageRequest.of(0, pageSize + 1)


        val postLikes = if (cursorId == null ) {
            postLikeRepository.findAllByUserId(user, pageable)
        } else {
            postLikeRepository.findAllByUserIdAndIdGreaterThan(user, cursorId, pageable)
        }.filterNot {
            reportPostRepository.existsByPostIdAndUserId(it.postId, user)
        }


        val postLikeList: List<PostLikeResponse> = postLikes.map { like ->
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
            totalPage = (postLikes.size / pageSize).toLong() + if (postLikes.size % pageSize > 0) 1 else 0,
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

        return PaginationPostLikeResponse(pagination =  pagination, items = postLikeList.take(pageSize), lastItemId = nextCursorId)
    }
}