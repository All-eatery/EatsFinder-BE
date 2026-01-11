package com.eatsfinder.domain.contact.dto

import com.eatsfinder.domain.contact.model.ContactStatus
import com.eatsfinder.domain.contact.model.Queries
import java.time.LocalDateTime

data class QResponse(
    val id: Long,
    val title: String,
    val content: String,
    val imgUrl: String,
    val status: ContactStatus,
    val createdAt: LocalDateTime,
    val answers: List<AResponse>
) {
    companion object {
        fun from(queries: Queries): QResponse {
            return QResponse(
                id = queries.id!!,
                title = queries.title,
                content = queries.content,
                imgUrl = queries.imgUrl,
                status = queries.status,
                createdAt = queries.createdAt,
                answers = queries.answers.map { answer ->
                    AResponse(
                        id = answer.id!!,
                        title = answer.title,
                        content = answer.content,
                        createdAt = answer.createdAt
                    )
                }
            )
        }
    }
}
