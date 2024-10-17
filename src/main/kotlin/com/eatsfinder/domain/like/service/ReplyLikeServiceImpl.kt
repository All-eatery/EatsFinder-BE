package com.eatsfinder.domain.like.service

import com.eatsfinder.domain.like.model.ReplyLikes
import com.eatsfinder.domain.like.repository.ReplyLikeRepository
import com.eatsfinder.domain.reply.repository.ReplyRepository
import com.eatsfinder.domain.user.model.MyActiveType
import com.eatsfinder.domain.user.model.UserLog
import com.eatsfinder.domain.user.repository.UserLogRepository
import com.eatsfinder.domain.user.repository.UserRepository
import com.eatsfinder.global.exception.ModelNotFoundException
import com.eatsfinder.global.exception.like.DefaultZeroException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReplyLikeServiceImpl(
    private val replyLikesRepository: ReplyLikeRepository,
    private val replyRepository: ReplyRepository,
    private val userRepository: UserRepository,
    private val userLogRepository: UserLogRepository
) : ReplyLikeService {

    @Transactional
    override fun createReplyLikes(userId: Long, replyId: Long) {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException(
            "user",
            "이 유저 아이디(${userId})는 존재하지 않습니다."
        )

        val reply = replyRepository.findByIdAndDeletedAt(replyId, null) ?: throw ModelNotFoundException(
            "reply",
            "이 유저 아이디(${userId})는 존재하지 않습니다."
        )

        val replyLike = replyLikesRepository.findByUserIdAndReplyId(user, reply)

        if (reply.likeCount < 0) throw DefaultZeroException("좋아요 수의 기본값은 0입니다.")

        if (replyLike == null) {
            reply.likeCount++
            replyRepository.save(reply)
            userLogRepository.save(
                UserLog(
                    userId = user,
                    postLikeId = null,
                    commentLikeId = null,
                    commentId = null,
                    replyId = null,
                    replyLikeId = ReplyLikes(userId = user, replyId = reply),
                    myActiveType = MyActiveType.REPLY_LIKES
                )
            )
        } else {
            throw ModelNotFoundException("like", "좋아요(${replyId})는 한번 밖에 하지 못합니다.")
        }
    }

    @Transactional
    override fun deleteReplyLikes(userId: Long, replyId: Long) {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException(
            "user",
            "이 유저 아이디(${userId})는 존재하지 않습니다."
        )

        val reply = replyRepository.findByIdAndDeletedAt(replyId, null) ?: throw ModelNotFoundException(
            "reply",
            "이 유저 아이디(${userId})는 존재하지 않습니다."
        )

        val replyLike = replyLikesRepository.findByUserIdAndReplyId(user, reply)


        if (replyLike != null) {
            if (reply.likeCount > 0) {
                replyLikesRepository.delete(replyLike)
                reply.likeCount--
                replyRepository.save(reply)

                val userLog =
                    userLogRepository.findUserLogByReplyLikeIdAndMyActiveType(replyLike, MyActiveType.REPLY_LIKES)
                        ?: throw ModelNotFoundException("userLog")
                userLogRepository.delete(userLog)
            } else {
                throw DefaultZeroException("좋아요 수의 기본값은 0입니다.")
            }
        } else {
            throw ModelNotFoundException("like", "좋아요(${replyId})는 존재하지 않아 취소할 수 없습니다.")
        }
    }
}