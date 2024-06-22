package com.eatsfinder.domain.user.dto

import com.eatsfinder.domain.user.model.SocialType

open class OAuth2UserInfo (
    val provider: SocialType,
    val nickname: String,
    val email: String,
    val profileImage: String
)