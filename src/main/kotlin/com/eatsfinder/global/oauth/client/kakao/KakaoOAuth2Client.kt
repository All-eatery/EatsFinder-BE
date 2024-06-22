package com.eatsfinder.global.oauth.client.kakao

import com.eatsfinder.domain.user.dto.OAuth2UserInfo
import com.eatsfinder.domain.user.model.SocialType
import com.eatsfinder.global.oauth.client.OAuth2Client
import com.eatsfinder.global.oauth.client.kakao.dto.KakaoTokenResponse
import com.eatsfinder.global.oauth.client.kakao.dto.KakaoUserInfoResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class KakaoOAuth2Client(
    private val restClient: RestClient,
    @Value("\${oauth2.kakao.client_id}") val clientId: String,
    @Value("\${oauth2.kakao.redirect_url}") val redirectUrl: String,
    @Value("\${oauth2.kakao.auth_server_base_url}") val authServerBaseUrl: String,
    @Value("\${oauth2.kakao.resource_server_base_url}") val resourceServerBaseUrl: String
) : OAuth2Client {
    override fun generateLoginPageUrl(): String {
        return StringBuilder(authServerBaseUrl)
            .append("/oauth/authorize")
            .append("?client_id=").append(clientId)
            .append("&redirect_uri=").append(redirectUrl)
            .append("&response_type=").append("code")
            .toString()
    }

    override fun getAccessToken(authorizationCode: String): String {
        val requestData = mutableMapOf(
            "grant_type" to "authorization_code",
            "client_id" to clientId,
            "code" to authorizationCode,
            "redirect_uri" to redirectUrl
        )
        return restClient.post()
            .uri("$authServerBaseUrl/oauth/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(LinkedMultiValueMap<String, String>().apply { this.setAll(requestData) })
            .retrieve()
            .body<KakaoTokenResponse>()
            ?.accessToken
            ?: throw RuntimeException("유저 조회 실패")
    }

    override fun retrieveUserInfo(accessToken: String): OAuth2UserInfo {
        return restClient.get()
            .uri("$resourceServerBaseUrl/v2/user/me")
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .body<KakaoUserInfoResponse>()
            ?: throw RuntimeException("유저 정보 조회 실패")
    }

    override fun supports(provider: SocialType): Boolean {
        return provider == SocialType.KAKAO
    }
}