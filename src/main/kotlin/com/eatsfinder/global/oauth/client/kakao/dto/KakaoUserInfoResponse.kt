package com.eatsfinder.global.oauth.client.kakao.dto

import com.eatsfinder.domain.user.dto.OAuth2UserInfo
import com.eatsfinder.domain.user.model.SocialType
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class KakaoUserInfoResponse(
    properties: KakaoUserPropertiesResponse,
    kakaoAccount: KakaoUserAccountResponse

) : OAuth2UserInfo(
    provider = SocialType.KAKAO,
    nickname = properties.nickname,
    email = kakaoAccount.email,
    profileImage = properties.profileImage
)