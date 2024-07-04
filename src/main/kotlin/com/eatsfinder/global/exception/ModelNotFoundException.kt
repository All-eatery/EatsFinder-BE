package com.eatsfinder.global.exception

data class ModelNotFoundException(
    val id: Any,
    val modelName: String
) : RuntimeException(
    "이 $modelName 으로는 주어진 ID: $id 찾을 수 없습니다."
)
