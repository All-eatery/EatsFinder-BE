package com.eatsfinder.global.exception.profile

class EnterAddInfoException(
    val fieldName: String,
    message: String = "EnterAddInfo"
) : RuntimeException(message)
