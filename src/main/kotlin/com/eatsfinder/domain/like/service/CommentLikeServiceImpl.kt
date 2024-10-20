package com.eatsfinder.domain.like.service

import com.eatsfinder.domain.comment.repository.CommentRepository
import com.eatsfinder.domain.like.model.CommentLikes
import com.eatsfinder.domain.like.repository.CommentLikeRepository
import com.eatsfinder.domain.user.model.MyActiveType
import com.eatsfinder.domain.user.model.UserLog
import com.eatsfinder.domain.user.repository.UserLogRepository
import com.eatsfinder.domain.user.repository.UserRepository
import com.eatsfinder.global.exception.ModelNotFoundException
import com.eatsfinder.global.exception.like.DefaultZeroException
import com.eatsfinder.global.exception.profile.MyProfileException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentLikeServiceImpl(
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository,
    private val commentLikeRepository: CommentLikeRepository,
    private val userLogRepository: UserLogRepository
) : CommentLikeService {


    @Transactional
    override fun createCommentLikes(userId: Long, commentId: Long) {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException(
            "user",
            "이 유저 아이디(${userId})는 존재하지 않습니다."
        )
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException(
            "comment",
            "이 댓글 아이디: (${commentId})는 존재하지 않습니다."
        )
        val commentLike = commentLikeRepository.findByUserIdAndCommentId(user, comment)

        if (comment.userId.id == user.id) {
            throw MyProfileException("본인 댓글이므로 좋아요를 할 수 없습니다.")
        }

        if (comment.likeCount < 0) throw DefaultZeroException("좋아요 수의 기본값은 0입니다.")

        if (commentLike == null) {
            val nowLikeCount = commentRepository.getLikeCountById(commentId)
            val newLikeCount = nowLikeCount + 1
            commentRepository.updateLikeCount(commentId, newLikeCount)
            userLogRepository.save(
                UserLog(
                    userId = user,
                    postLikeId = null,
                    commentLikeId = CommentLikes(userId = user, commentId = comment),
                    commentId = null,
                    replyId = null,
                    replyLikeId = null,
                    myActiveType = MyActiveType.COMMENT_LIKES
                )
            )
        } else {
            throw ModelNotFoundException("like", "좋아요(${commentId})는 한번 밖에 하지 못합니다.")
        }
    }

    @Transactional
    override fun deleteCommentLikes(userId: Long, commentId: Long) {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException(
            "user",
            "이 유저 아이디(${userId})는 존재하지 않습니다."
        )
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException(
            "post",
            "이 댓글 아이디: (${commentId})는 존재하지 않습니다."
        )
        val commentLike = commentLikeRepository.findByUserIdAndCommentId(user, comment)

        if (comment.userId.id == user.id) {
            throw MyProfileException("본인 댓글이므로 좋아요를 취소할 수 없습니다.")
        }

        if (commentLike != null) {
            if (comment.likeCount > 0) {
                val nowLikeCount = commentRepository.getLikeCountById(commentId)
                val newLikeCount = nowLikeCount + 1
                commentRepository.updateLikeCount(commentId, newLikeCount)

                commentLikeRepository.delete(commentLike)
                commentRepository.deleteUserLogCommentLikeId(commentId)

                val userLog =
                    userLogRepository.findUserLogByCommentLikeIdAndMyActiveType(commentLike, MyActiveType.COMMENT_LIKES)
                        ?: throw ModelNotFoundException("userLog")
                userLogRepository.delete(userLog)
            } else {
                throw DefaultZeroException("좋아요 수의 기본값은 0입니다.")
            }
        } else {
            throw ModelNotFoundException("like", "좋아요(${commentId})는 존재하지 않아 취소할 수 없습니다.")
        }
    }
}