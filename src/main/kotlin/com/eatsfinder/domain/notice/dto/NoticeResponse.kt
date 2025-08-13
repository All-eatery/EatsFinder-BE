package com.eatsfinder.domain.notice.dto

import com.eatsfinder.domain.notice.model.Notice
import java.time.LocalDateTime

data class NoticeResponse(
    val id : Long?,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime
){
    companion object {
        fun from(notice: Notice): NoticeResponse {
            return NoticeResponse(
                id = notice.id,
                title = notice.title,
                content = notice.content,
                createdAt = notice.createdAt
            )
        }
    }
}
