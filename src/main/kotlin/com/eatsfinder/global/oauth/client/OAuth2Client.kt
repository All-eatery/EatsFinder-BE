package com.eatsfinder.global.oauth.client

import com.eatsfinder.domain.user.dto.oauth.OAuth2UserInfo
import com.eatsfinder.domain.user.model.SocialType

interface OAuth2Client {
    fun generateLoginPageUrl(): String
    fun getAccessToken(authorizationCode: String): String
    fun retrieveUserInfo(accessToken: String): OAuth2UserInfo
    fun supports(provider: SocialType): Boolean
}