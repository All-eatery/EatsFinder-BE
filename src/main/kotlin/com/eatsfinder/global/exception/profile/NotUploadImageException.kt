package com.eatsfinder.global.exception.profile

class NotUploadImageException(
    val fieldName: String,
    message: String = "NotUploadImage"
) : RuntimeException(message)
