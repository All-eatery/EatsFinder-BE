package com.eatsfinder.global.exception.email

class NotGenerateCodeException(
    val fieldName: String,
    message: String = "NotGenerateCode"
) : RuntimeException(message)
