package com.eatsfinder.global.oauth.client.google.dto

import com.eatsfinder.domain.user.dto.OAuth2UserInfo
import com.eatsfinder.domain.user.model.SocialType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class GoogleUserInfoResponse(
    name: String,
    picture: String,

    @JsonProperty
    email: String

) : OAuth2UserInfo(
    provider = SocialType.GOOGLE,
    nickname = name,
    email = email,
    profileImage = picture
)