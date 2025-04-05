package com.eatsfinder.domain.comment.service

import com.eatsfinder.domain.comment.dto.CommentRequest
import com.eatsfinder.domain.comment.dto.CommentsResponse
import com.eatsfinder.domain.comment.dto.CommentsResponse.Companion.from
import com.eatsfinder.domain.comment.model.Comment
import com.eatsfinder.domain.comment.repository.CommentRepository
import com.eatsfinder.domain.follow.dto.FollowerListResponse
import com.eatsfinder.domain.like.repository.CommentLikeRepository
import com.eatsfinder.domain.like.repository.ReplyLikeRepository
import com.eatsfinder.domain.post.repository.PostRepository
import com.eatsfinder.domain.reply.repository.ReplyRepository
import com.eatsfinder.domain.report.repository.ReportCommentRepository
import com.eatsfinder.domain.report.repository.ReportPostRepository
import com.eatsfinder.domain.user.model.MyActiveType
import com.eatsfinder.domain.user.model.UserLog
import com.eatsfinder.domain.user.repository.UserLogRepository
import com.eatsfinder.domain.user.repository.UserRepository
import com.eatsfinder.global.exception.ModelNotFoundException
import com.eatsfinder.global.exception.profile.ImmutableUserOrUnauthorizedUserException
import com.eatsfinder.global.pagination.PaginationCursorItemsResponse
import com.eatsfinder.global.security.jwt.UserPrincipal
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentServiceImpl(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val commentLikeRepository: CommentLikeRepository,
    private val userLogRepository: UserLogRepository,
    private val replyLikeRepository: ReplyLikeRepository,
    private val reportCommentRepository: ReportCommentRepository,
    private val reportPostRepository: ReportPostRepository
) : CommentService {

    @Transactional(readOnly = true)
    override fun getCommentList(
        postId: Long,
        userPrincipal: UserPrincipal?,
        userId: Long?,
        cursorId: Long?,
        pageSize: Int
    ): CommentsResponse {
        val loginUser = userPrincipal?.id

        val user = loginUser?.let {
            userRepository.findByIdAndDeletedAt(it, null) ?: throw ModelNotFoundException(
                "user",
                "이 유저 아이디(${userId})는 존재하지 않습니다."
            )
        }

        val post = postRepository.findByIdAndDeletedAt(postId, null) ?: throw ModelNotFoundException(
            "post",
            "이 게시물 아이디: (${postId})는 존재하지 않습니다."
        )

        val pageable: Pageable = PageRequest.of(0, pageSize + 1)

        val comments = if (cursorId == null ) {
            commentRepository.findByPostIdAndDeletedAt(post, null)
        } else {
            commentRepository.findAllByPostIdAndIdGreaterThan(post, cursorId, pageable)
        }.filterNot {
            reportPostRepository.existsByPostIdAndUserId(it.postId, user)
        }

        comments.forEach { comment ->
            comment.replies =
                comment.replies.filterNot { reportCommentRepository.existsByReplyIdAndUserId(it, user) }.toMutableList()
        }

        val nextCursorId = when {
            comments.isEmpty() -> null
            comments.size > pageSize -> comments[pageSize - 1].id
            else -> comments.last().id
        }

        val totalCount = commentRepository.countByPostIdAndDeletedAt(post, null)

        val reportedPost = reportPostRepository.findFirstByPostIdAndReportedUserId(post, post.userId)
        if (reportedPost != null && reportedPost.userId.id == loginUser) return CommentsResponse(null, emptyList(), nextCursorId)


        val commentLikes = user?.let { commentLikeRepository.findCommentLikesByUserId(it) } ?: emptyList()
        val replyLikes = user?.let { replyLikeRepository.findReplyLikesByUserId(it) } ?: emptyList()

        return from(totalCount, pageSize, comments, userPrincipal, commentLikes, replyLikes, post)
    }

    @Transactional
    override fun createComment(postId: Long, req: CommentRequest, userId: Long): String {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException(
            "user",
            "이 유저 아이디(${userId})는 존재하지 않습니다."
        )
        val post = postRepository.findByIdAndDeletedAt(postId, null) ?: throw ModelNotFoundException(
            "post",
            "이 게시물 아이디: (${postId})는 존재하지 않습니다."
        )
        userLogRepository.save(
            UserLog(
                userId = user,
                postLikeId = null,
                commentLikeId = null,
                commentId = Comment(
                    content = req.content,
                    userId = user,
                    postId = post
                ),
                replyId = null,
                replyLikeId = null,
                myActiveType = MyActiveType.COMMENT
            )
        )
        return "댓글이 작성되었습니다."
    }

    @Transactional
    override fun updateComment(req: CommentRequest, userId: Long, commentId: Long): String {
        val comment = commentRepository.findByIdAndDeletedAt(commentId, null) ?: throw ModelNotFoundException(
            "comment",
            "이 댓글(${commentId})은 존재하지 않습니다."
        )

        if (comment.userId.id != userId) {
            throw ImmutableUserOrUnauthorizedUserException("이 댓글을 수정할 권한이 없습니다.")
        }

        comment.content = req.content
        commentRepository.save(comment)
        return "댓글이 수정되었습니다."
    }

    @Transactional
    override fun deleteComment(commentId: Long, userId: Long) {
        val comment = commentRepository.findByIdAndDeletedAt(commentId, null) ?: throw ModelNotFoundException(
            "comment",
            "이 댓글(${commentId})은 존재하지 않습니다."
        )
        if (comment.userId.id != userId) {
            throw ImmutableUserOrUnauthorizedUserException("이 댓글을 삭제할 권한이 없습니다.")
        }

        commentRepository.delete(comment)
        val userLog = userLogRepository.findUserLogByCommentIdAndMyActiveType(comment, MyActiveType.COMMENT)
            ?: throw ModelNotFoundException("userLog")
        userLogRepository.delete(userLog)
    }

//    private fun paginationComment(cursorId: Long?, pageSize: Int): {
//
//        val pageable = PageRequest.of(0, pageSize + 1)
//
//        val follows = commentRepository.findAllByFollowedUserIdAndFollowingUserIdDeletedAtIsNull(user, pageable)
//
//        val hasNext = hasNext(follows.size, pageSize)
//
//        val pagination = PaginationCursorItemsResponse(
//            cursorId = cursorId,
//            totalItems = follows.size.toLong(),
//            itemsPerPage = pageSize,
//            totalPage = (follows.size / pageSize).toLong() + if (follows.size % pageSize > 0) 1 else 0,
//            currentPage = 1,
//            isLastPage = !hasNext
//        )
//
//        val followList: List<FollowerListResponse> = follows.map { follow ->
//            FollowerListResponse(
//                followerUserId = follow.followedUserId.id!!,
//                followerUserNickname = follow.followedUserId.nickname,
//                imageUrl = follow.followingUserId.profileImage
//            )
//        }
//
//        return PaginationFollowerResponse(
//            followList = followList,
//            pagination = pagination
//        )
//    }

    private fun hasNext(followsSize: Int, pageSize: Int): Boolean {
//        if (followsSize == 0) {
//            throw EntityNotFoundException(Follow::class.java)
//        }
        return followsSize > pageSize
    }
}
