package com.eatsfinder.global.exception.report

class ReportReasonException(
    val fieldName: String,
    message: String = "ReportReason"
) : RuntimeException(message)
