package com.eatsfinder.domain.report.repository

import com.eatsfinder.domain.comment.model.Comment
import com.eatsfinder.domain.reply.model.Reply
import com.eatsfinder.domain.report.model.ReportComment
import com.eatsfinder.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface ReportCommentRepository : JpaRepository<ReportComment, Long> {
    fun existsByIdAndReportedUserIdAndUserId(id: Long, reportedUserId: User, userId: User?): Boolean

    fun existsByCommentIdAndUserId(commentId: Comment?, userId: User?): Boolean

    fun existsByReplyIdAndUserId(replyId: Reply?, userId: User?): Boolean


}