package com.eatsfinder.global.exception.profile

class NotMyProfileException(
    val fieldName: String,
    message: String = "NotMyProfile"
) : RuntimeException(message)
