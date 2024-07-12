package com.eatsfinder.global.exception.profile

class NotMismatchProfileImageException(
    val fieldName: String,
    message: String = "NotMismatchProfileImage"
) : RuntimeException(message)
