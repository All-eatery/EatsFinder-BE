package com.eatsfinder.global.exception.dto

import com.eatsfinder.global.exception.status.StatusCode

data class BaseResponse<T>(
    val statusCode: String = StatusCode.SUCCESS.name,
    val data: T? = null,
    val message: String = StatusCode.SUCCESS.message,
)