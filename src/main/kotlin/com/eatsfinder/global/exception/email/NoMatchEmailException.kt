package com.eatsfinder.global.exception.email

class NoMatchEmailException(
    val fieldName: String,
    message: String = "NoMatchEmail"
) : RuntimeException(message)