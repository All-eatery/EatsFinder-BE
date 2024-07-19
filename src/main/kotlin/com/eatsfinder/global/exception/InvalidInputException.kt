package com.eatsfinder.global.exception

class InvalidInputException(
    val fieldName: String,
    message: String = "InvalidInput"
) : RuntimeException(message)
