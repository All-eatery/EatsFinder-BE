package com.eatsfinder.global.security.jwt

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.web.authentication.WebAuthenticationDetails

class JwtAuthenticationToken(
    private val principal: UserPrincipal,
    detail: WebAuthenticationDetails
) : AbstractAuthenticationToken(principal.authorities) {
    init {
        super.setAuthenticated(true)
        super.setDetails(detail)
    }

    override fun getCredentials() = null
    override fun getPrincipal() = principal

    override fun isAuthenticated(): Boolean {
        return true
    }

}