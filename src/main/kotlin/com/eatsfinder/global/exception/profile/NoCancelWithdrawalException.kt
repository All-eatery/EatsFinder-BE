package com.eatsfinder.global.exception.profile

class NoCancelWithdrawalException(
    val fieldName: String,
    message: String = "NoCancelWithdrawal"
) : RuntimeException(message)