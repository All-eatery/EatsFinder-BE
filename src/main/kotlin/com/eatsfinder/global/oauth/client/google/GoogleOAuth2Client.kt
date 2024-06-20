package com.eatsfinder.global.oauth.client.google

import com.eatsfinder.domain.user.dto.OAuth2UserInfo
import com.eatsfinder.domain.user.model.SocialType
import com.eatsfinder.global.oauth.client.OAuth2Client
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class GoogleOAuth2Client(
    private val restClient: RestClient
) : OAuth2Client {
    override fun generateLoginPageUrl(): String {
        TODO("Not yet implemented")
    }

    override fun getAccessToken(authorizationCode: String): String {
        TODO("Not yet implemented")
    }

    override fun retrieveUserInfo(accessToken: String): OAuth2UserInfo {
        TODO("Not yet implemented")
    }

    override fun supports(provider: SocialType): Boolean {
        TODO("Not yet implemented")
    }
}