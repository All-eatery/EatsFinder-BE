package com.eatsfinder.global.exception

class AlreadyExistException(
    val fieldName: String,
    message: String = "AlreadyExist"
) : RuntimeException(message)