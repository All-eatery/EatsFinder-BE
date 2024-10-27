package com.eatsfinder.domain.report.service

import com.eatsfinder.domain.report.dto.ReportCommentRequest
import com.eatsfinder.domain.report.dto.ReportPostRequest

interface ReportService {

    fun reportPost(req: ReportPostRequest, userId: Long, postId: Long): String

    fun reportComment(req: ReportCommentRequest, userId: Long, commentId: Long): String

    fun reportReply(req: ReportCommentRequest, userId: Long, replyId: Long): String
}