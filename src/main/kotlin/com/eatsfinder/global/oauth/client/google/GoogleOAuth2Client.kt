package com.eatsfinder.global.oauth.client.google

import com.eatsfinder.domain.user.model.SocialType
import com.eatsfinder.global.oauth.client.OAuth2Client
import com.eatsfinder.global.oauth.client.google.dto.GoogleTokenResponse
import com.eatsfinder.global.oauth.client.google.dto.GoogleUserInfoResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class GoogleOAuth2Client(
    private val restClient: RestClient,
    @Value("\${oauth2.google.client_id}") val clientId: String,
    @Value("\${oauth2.google.redirect_url}") val redirectUrl: String,
    @Value("\${oauth2.google.client_secret}") val clientSecret: String
) : OAuth2Client {
    override fun generateLoginPageUrl(): String {
        return StringBuilder(GOOGLE_AUTH_BASE_URL)
            .append("?response_type=").append("code")
            .append("&client_id=").append(clientId)
            .append("&redirect_uri=").append(redirectUrl)
            .append("&scope=")
            .append("https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email")
            .toString()
    }

    override fun getAccessToken(authorizationCode: String): String {
        val requestData = mutableMapOf(
            "grant_type" to "authorization_code",
            "client_id" to clientId,
            "client_secret" to clientSecret,
            "code" to authorizationCode,
            "redirect_uri" to redirectUrl
        )
        return restClient.post()
            .uri("$GOOGLE_TOKEN_BASE_URL/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(LinkedMultiValueMap<String, String>().apply { this.setAll(requestData) })
            .retrieve()
            .body<GoogleTokenResponse>()
            ?.accessToken
            ?: throw RuntimeException("유저 조회 실패")
    }

    override fun retrieveUserInfo(accessToken: String): GoogleUserInfoResponse {
        return restClient.get()
            .uri("$GOOGLE_API_BASE_URL/userinfo")
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .body<GoogleUserInfoResponse>()
            ?: throw RuntimeException("유저 조회 실패")
    }

    override fun supports(provider: SocialType): Boolean {
        return provider == SocialType.GOOGLE
    }

    companion object {
        private const val GOOGLE_AUTH_BASE_URL = "https://accounts.google.com/o/oauth2/v2/auth"
        private const val GOOGLE_API_BASE_URL = "https://www.googleapis.com/oauth2/v2"
        private const val GOOGLE_TOKEN_BASE_URL = "https://oauth2.googleapis.com"
    }
}