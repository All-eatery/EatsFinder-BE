package com.eatsfinder.global.exception.profile

class WithdrawalReasonException(
    val fieldName: String,
    message: String = "EnterAddInfo"
) : RuntimeException(message)
