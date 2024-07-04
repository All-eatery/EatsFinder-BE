package com.eatsfinder.global.exception.email

class OneTimeMoreWriteException(
    val fieldName: String,
    message: String = "OneTimeMoreWrite"
) : RuntimeException(message)
