package com.eatsfinder.global.exception

class ModelNotFoundException(
    val fieldName: String,
    message: String = "ModelNotFound"
) : RuntimeException(message)
