package com.eatsfinder.domain.user.controller

import com.eatsfinder.domain.user.dto.oauth.OAuthResponse
import com.eatsfinder.domain.user.model.SocialType
import com.eatsfinder.domain.user.service.OAuth2LoginService
import com.eatsfinder.global.oauth.client.OAuth2ClientService
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.HttpHeaders
import java.net.URI

@RestController
@RequestMapping("/auth")
class OAuth2LoginController(
    private val oAuth2LoginService: OAuth2LoginService,
    private val oAuth2Client: OAuth2ClientService
) {

    @Operation(summary = "소셜 로그인 (로그인 페이지로 Redirect 하기)")
    @PreAuthorize("isAnonymous()")
    @GetMapping("/login/{provider}")
    fun oAuth2Login(
        @PathVariable provider: SocialType, res: HttpServletResponse
    ) {
        val loginUrl = oAuth2Client.generateLoginPageUrl(provider)
        res.sendRedirect(loginUrl)
    }

    @Operation(summary = "소셜 타입 확인 후 AccessToken 반환")
    @PreAuthorize("isAnonymous()")
    @GetMapping("/callback/{provider}")
    fun callback(
        @PathVariable provider: SocialType,
        @RequestParam(name = "code") authorizationCode: String
    ): ResponseEntity<OAuthResponse> {

        val accessToken = oAuth2LoginService.login(provider, authorizationCode)
        val oauthResponse = OAuthResponse(accessToken)

        return ResponseEntity(oauthResponse, HttpStatus.OK)
    }
}