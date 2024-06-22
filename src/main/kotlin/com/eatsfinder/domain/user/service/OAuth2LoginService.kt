package com.eatsfinder.domain.user.service

import com.eatsfinder.domain.user.model.SocialType
import com.eatsfinder.global.oauth.client.OAuth2ClientService
import com.eatsfinder.global.security.jwt.JwtPlugin
import org.springframework.stereotype.Service

@Service
class OAuth2LoginService(
    private val oAuth2ClientService: OAuth2ClientService,
    private val oAuthUserService: OAuthUserService,
    private val jwtPlugin: JwtPlugin
) {
    fun login(provider: SocialType, authorizationCode: String): String {
        return oAuth2ClientService.getUserInfo(provider, authorizationCode)
            .let { oAuthUserService.registerIfNotExist(it) }
            .let { jwtPlugin.generateAccessToken(it.id!!.toString(), it.email, it.role.toString()) }
    }
}