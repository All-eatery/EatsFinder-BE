package com.eatsfinder.global.exception.profile

class NoChangeNicknameAtException(
    val fieldName: String,
    message: String = "NoChangeNicknameAt"
) : RuntimeException(message)