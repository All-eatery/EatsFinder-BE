package com.eatsfinder.domain.notice.service

import com.eatsfinder.domain.notice.dto.NoticeRequest
import com.eatsfinder.domain.notice.dto.NoticeResponse

interface NoticeService {

    fun getNoticeList(userId: Long): List<NoticeResponse>

    fun getNotice(noticeId: Long, userId: Long): NoticeResponse

    fun createNotice(req: NoticeRequest, userId: Long): String

    fun updateNotice(req: NoticeRequest, userId: Long, noticeId: Long): String

    fun deleteNotice(noticeId: Long, userId: Long): String
}