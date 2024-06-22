package com.eatsfinder.global.oauth.client.kakao.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoUserPropertiesResponse(
    val nickname: String,
    val profileImage: String
)
