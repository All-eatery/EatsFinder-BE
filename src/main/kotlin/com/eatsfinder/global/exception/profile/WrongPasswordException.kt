package com.eatsfinder.global.exception.profile

class WrongPasswordException(
    val fieldName: String,
    message: String = "WrongPassword"
) : RuntimeException(message)
