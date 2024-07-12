package com.eatsfinder.global.oauth.client

import com.eatsfinder.domain.user.dto.oauth.OAuth2UserInfo
import com.eatsfinder.domain.user.model.SocialType
import org.springframework.stereotype.Component

@Component
class OAuth2ClientService(
    private val clientList: List<OAuth2Client>
) {
    fun generateLoginPageUrl(provider: SocialType): String {
        val client = this.chooseClient(provider)
        return client.generateLoginPageUrl()
    }

    fun getUserInfo(provider: SocialType, authorizationCode: String): OAuth2UserInfo {
        val client = this.chooseClient(provider)
        return client.getAccessToken(authorizationCode)
            .let { client.retrieveUserInfo(it) }
    }

    private fun chooseClient(provider: SocialType): OAuth2Client {
        return clientList.find { it.supports(provider) }
            ?: throw RuntimeException("지원하지 않는 OAUTH TYPE 입니다.")
    }
}