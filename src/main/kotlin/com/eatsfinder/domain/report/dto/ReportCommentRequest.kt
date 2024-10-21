package com.eatsfinder.domain.report.dto

data class ReportCommentRequest(
    val abusive: Boolean,
    val spam: Boolean,
    val porno: Boolean,
    val offensive: Boolean,
    val harmful: Boolean,
    val others: Boolean,
    val reason: String?
)
