package com.eatsfinder.global.exception.profile

class AlreadyDefaultProfileImageException(
    val fieldName: String,
    message: String = "AlreadyDefaultProfileImage"
) : RuntimeException(message)
