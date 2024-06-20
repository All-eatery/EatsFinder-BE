package com.eatsfinder.domain.user.dto

import com.eatsfinder.domain.user.model.SocialType

open class OAuth2UserInfo (
    val provider: SocialType,
    val id: String,
    val nickname: String,
    val email: String
)