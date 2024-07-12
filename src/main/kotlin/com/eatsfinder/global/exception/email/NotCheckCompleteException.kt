package com.eatsfinder.global.exception.email

class NotCheckCompleteException(
    val fieldName: String,
    message: String = "NotCheckComplete"
) : RuntimeException(message)
