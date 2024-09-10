package com.eatsfinder.domain.user.dto.user

data class DeleteReasonRequest(
    val email: String,
    val code: String,
    val reason: String?
)
