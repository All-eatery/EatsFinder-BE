package com.eatsfinder.global.exception.email

class ExpiredCodeException (
    val fieldName: String,
    message: String = "ExpiredCode"
) : RuntimeException(message)