package com.eatsfinder.domain.report.dto

data class ReportPostRequest(
    val differentPlace: Boolean,
    val differentMenu: Boolean,
    val sameReview: Boolean,
    val differentPrice: Boolean,
    val closed: Boolean,
    val others: Boolean,
    val reason: String?
)
