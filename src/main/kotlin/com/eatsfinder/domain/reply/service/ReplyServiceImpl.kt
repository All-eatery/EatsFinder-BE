package com.eatsfinder.domain.reply.service

import com.eatsfinder.domain.comment.repository.CommentRepository
import com.eatsfinder.domain.reply.dto.ReplyRequest
import com.eatsfinder.domain.reply.model.Reply
import com.eatsfinder.domain.reply.repository.ReplyRepository
import com.eatsfinder.domain.user.model.MyActiveType
import com.eatsfinder.domain.user.model.UserLog
import com.eatsfinder.domain.user.repository.UserLogRepository
import com.eatsfinder.domain.user.repository.UserRepository
import com.eatsfinder.global.exception.ModelNotFoundException
import com.eatsfinder.global.exception.profile.ImmutableUserOrUnauthorizedUserException
import org.springframework.stereotype.Service

@Service
class ReplyServiceImpl(
    private val replyRepository: ReplyRepository,
    private val commentRepository: CommentRepository,
    private val userLogRepository: UserLogRepository,
    private val userRepository: UserRepository,
): ReplyService {
    override fun createReply(commentId: Long, req: ReplyRequest, userId: Long): String {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException(
            "user",
            "이 유저 아이디(${userId})는 존재하지 않습니다."
        )

        val comment = commentRepository.findByIdAndDeletedAt(commentId, null) ?: throw ModelNotFoundException(
            "comment",
            "이 댓글 아이디(${commentId})는 존재하지 않습니다."
        )

        userLogRepository.save(
            UserLog(
                userId = user,
                postLikeId = null,
                commentLikeId = null,
                commentId = null,
                replyId = Reply(
                    content = req.content,
                    userId = user,
                    commentId = comment
                ),
                replyLikeId = null,
                myActiveType = MyActiveType.REPLY
            )
        )
        return "대댓글이 작성되었습니다."
    }

    override fun updateReply(req: ReplyRequest, userId: Long, replyId: Long): String {
        val reply = replyRepository.findByIdAndDeletedAt(replyId, null) ?: throw ModelNotFoundException(
            "reply",
            "이 대댓글(${replyId})은 존재하지 않습니다."
        )

        if (reply.userId.id != userId) {
            throw ImmutableUserOrUnauthorizedUserException("이 대댓글을 수정할 권한이 없습니다.")
        }

        reply.content = req.content
        replyRepository.save(reply)
        return "대댓글이 수정되었습니다."
    }

    override fun deleteReply(replyId: Long, userId: Long) {
        val reply = replyRepository.findByIdAndDeletedAt(replyId, null) ?: throw ModelNotFoundException(
            "reply",
            "이 대댓글(${replyId})은 존재하지 않습니다."
        )

        if (reply.userId.id != userId) {
            throw ImmutableUserOrUnauthorizedUserException("이 대댓글을 삭제할 권한이 없습니다.")
        }
        replyRepository.delete(reply)
    }
}