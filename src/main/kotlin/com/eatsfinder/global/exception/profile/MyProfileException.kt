package com.eatsfinder.global.exception.profile

class MyProfileException(
    val fieldName: String,
    message: String = "MyProfile"
) : RuntimeException(message)