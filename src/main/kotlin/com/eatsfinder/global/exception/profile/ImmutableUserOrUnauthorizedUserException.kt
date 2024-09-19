package com.eatsfinder.global.exception.profile

class ImmutableUserOrUnauthorizedUserException(
    val fieldName: String,
    message: String = "ImmutableUserOrUnauthorizedUser"
) : RuntimeException(message)
