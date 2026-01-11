package com.eatsfinder.domain.contact.dto

import java.time.LocalDateTime

data class AResponse(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime
)

