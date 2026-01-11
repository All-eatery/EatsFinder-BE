package com.eatsfinder.domain.contact.service

import com.eatsfinder.domain.contact.dto.ARequest
import com.eatsfinder.domain.contact.dto.QRequest
import com.eatsfinder.domain.contact.dto.QResponse
import com.eatsfinder.domain.notice.dto.NoticeRequest

interface ContactService {
    fun getQueryList(userId: Long): List<QResponse>

    fun getQuery(noticeId: Long, userId: Long): QResponse

    fun createQuery(req: NoticeRequest, userId: Long): String

    fun updateQuery(req: QRequest, userId: Long, queryId: Long): String

    fun deleteQuery(queryId: Long, userId: Long): String

    fun createAnswer(req: NoticeRequest, userId: Long): String

    fun updateAnswer(req: ARequest, userId: Long, answerId: Long): String

    fun deleteAnswer(answerId: Long, userId: Long): String

}