package com.eatsfinder.global.exception.like

class DefaultZeroException(
    val fieldName: String,
    message: String = "DefaultZero"
) : RuntimeException(message)
