package com.eatsfinder.global.exception.profile

class ImmutableUserException(
    val fieldName: String,
    message: String = "ImmutableUser"
) : RuntimeException(message)
