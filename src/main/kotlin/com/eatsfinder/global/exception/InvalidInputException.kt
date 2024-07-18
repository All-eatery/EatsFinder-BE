package com.eatsfinder.global.exception

class InvalidInputException(
    val fieldName: String,
    message: String = "ModelNotFound"
) : RuntimeException(message)
