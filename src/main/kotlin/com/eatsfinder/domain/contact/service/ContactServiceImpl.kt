package com.eatsfinder.domain.contact.service

import com.eatsfinder.domain.contact.dto.ARequest
import com.eatsfinder.domain.contact.dto.QRequest
import com.eatsfinder.domain.contact.dto.QResponse
import com.eatsfinder.domain.contact.repository.AnswerRepository
import com.eatsfinder.domain.contact.repository.QueryRepository
import com.eatsfinder.domain.notice.dto.NoticeRequest
import org.springframework.stereotype.Service

@Service
class ContactServiceImpl(
    private val answerRepository: AnswerRepository,
    private val queryRepository: QueryRepository
): ContactService {
    override fun getQueryList(userId: Long): List<QResponse> {
        TODO("Not yet implemented")
    }

    override fun getQuery(noticeId: Long, userId: Long): QResponse {
        TODO("Not yet implemented")
    }

    override fun createQuery(req: NoticeRequest, userId: Long): String {
        TODO("Not yet implemented")
    }

    override fun updateQuery(req: QRequest, userId: Long, queryId: Long): String {
        TODO("Not yet implemented")
    }

    override fun deleteQuery(queryId: Long, userId: Long): String {
        TODO("Not yet implemented")
    }

    override fun createAnswer(req: NoticeRequest, userId: Long): String {
        TODO("Not yet implemented")
    }

    override fun updateAnswer(req: ARequest, userId: Long, answerId: Long): String {
        TODO("Not yet implemented")
    }

    override fun deleteAnswer(answerId: Long, userId: Long): String {
        TODO("Not yet implemented")
    }
}