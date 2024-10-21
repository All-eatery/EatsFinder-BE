package com.eatsfinder.domain.report.service

import com.eatsfinder.domain.comment.repository.CommentRepository
import com.eatsfinder.domain.post.repository.PostRepository
import com.eatsfinder.domain.reply.repository.ReplyRepository
import com.eatsfinder.domain.report.dto.ReportCommentRequest
import com.eatsfinder.domain.report.dto.ReportPostRequest
import com.eatsfinder.domain.report.model.ReportComment
import com.eatsfinder.domain.report.model.ReportPost
import com.eatsfinder.domain.report.repository.ReportCommentRepository
import com.eatsfinder.domain.report.repository.ReportPostRepository
import com.eatsfinder.domain.user.repository.UserRepository
import com.eatsfinder.global.exception.AlreadyExistException
import com.eatsfinder.global.exception.InvalidInputException
import com.eatsfinder.global.exception.ModelNotFoundException
import com.eatsfinder.global.exception.report.ReportReasonException
import org.springframework.stereotype.Service

@Service
class ReportServiceImpl(
    private val reportPostRepository: ReportPostRepository,
    private val reportCommentRepository: ReportCommentRepository,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val replyRepository: ReplyRepository
): ReportService {
    override fun reportPost(req: ReportPostRequest, userId: Long, postId: Long): String {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException(
            "user",
            "이 유저 아이디(${userId})는 존재하지 않습니다."
        )

        val post = postRepository.findByIdAndDeletedAt(postId, null) ?: throw ModelNotFoundException(
            "post",
            "이 게시물(${postId})은 존재하지 않습니다."
        )

        val report = reportPostRepository.existsByPostIdAndReportedUserIdAndUserId(post, post.userId, user)
            if (report) throw AlreadyExistException("이미 신고 완료된 게시물입니다.")

        if (req.others && req.reason == null){
            throw ReportReasonException("게시물 신고 기타 사유를 입력해주세요")
        }

        if (post.userId.id == user.id){
            throw InvalidInputException("본인이 쓴 게시물을 신고할 수는 없습니다.")
        }

        if (!req.differentPlace && !req.differentMenu && !req.sameReview && !req.differentPrice && !req.closed && !req.others) {
            throw ReportReasonException("신고 사유를 하나 이상 선택해주세요.")
        }

        reportPostRepository.save(
            ReportPost(
                userId = user,
                postId = post,
                reportedUserId = post.userId,
                differentPlace = req.differentPlace,
                differentMenu = req.differentMenu,
                sameReview = req.sameReview,
                differentPrice = req.differentPrice,
                closed = req.closed,
                others = req.others,
                reason = req.reason!!
            )
        )

        return "이 게시물이 신고되었습니다."

    }

    override fun reportComment(req: ReportCommentRequest, userId: Long, commentId: Long): String {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException(
            "user",
            "이 유저 아이디(${userId})는 존재하지 않습니다."
        )

        val comment = commentRepository.findByIdAndDeletedAt(commentId, null) ?: throw ModelNotFoundException(
            "comment",
            "이 댓글(${commentId})은 존재하지 않습니다."
        )

        val report = reportCommentRepository.existsByIdAndReportedUserIdAndUserId(commentId, comment.userId, user)
        if (report) throw AlreadyExistException ("이미 신고 완료된 댓글입니다.")

        if (comment.userId.id == user.id){
            throw InvalidInputException("본인이 쓴 댓글을 신고할 수는 없습니다.")
        }

        if (req.others && req.reason == null){
            throw ReportReasonException("댓글 신고 기타 사유를 입력해주세요")
        }

        if (!req.abusive && !req.spam && !req.porno && !req.offensive && !req.harmful && !req.others) {
            throw ReportReasonException("신고 사유를 하나 이상 선택해주세요.")
        }

        reportCommentRepository.save(
            ReportComment(
                userId = user,
                commentId = comment,
                replyId = null,
                reportedUserId = comment.userId,
                abusive = req.abusive,
                spam = req.spam,
                porno = req.porno,
                offensive = req.offensive,
                harmful = req.harmful,
                others = req.others,
                reason = req.reason!!
            )
        )

        return "이 댓글이 신고되었습니다."
    }

    override fun reportReply(req: ReportCommentRequest, userId: Long, replyId: Long): String {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException(
            "user",
            "이 유저 아이디(${userId})는 존재하지 않습니다."
        )

        val reply = replyRepository.findByIdAndDeletedAt(replyId, null) ?: throw ModelNotFoundException(
            "reply",
            "이 대댓글(${replyId})은 존재하지 않습니다."
        )

        val report = reportCommentRepository.existsByIdAndReportedUserIdAndUserId(replyId, reply.userId, user)
        if (report) throw AlreadyExistException ("이미 신고 완료된 대댓글입니다.")

        if (reply.userId.id == user.id){
            throw InvalidInputException("본인이 쓴 대댓글을 신고할 수는 없습니다.")
        }

        if (req.others && req.reason == null){
            throw ReportReasonException("대댓글 신고 기타 사유를 입력해주세요")
        }

        if (!req.abusive && !req.spam && !req.porno && !req.offensive && !req.harmful && !req.others) {
            throw ReportReasonException("신고 사유를 하나 이상 선택해주세요.")
        }

        reportCommentRepository.save(
            ReportComment(
                userId = user,
                commentId = null,
                replyId = reply,
                reportedUserId = reply.userId,
                abusive = req.abusive,
                spam = req.spam,
                porno = req.porno,
                offensive = req.offensive,
                harmful = req.harmful,
                others = req.others,
                reason = req.reason!!
            )
        )

        return "이 대댓글이 신고되었습니다."
    }
}