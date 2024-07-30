package com.eatsfinder.domain.user.dto.oauth

import com.eatsfinder.domain.user.model.SocialType

open class OAuth2UserInfo (
    val provider: SocialType,
    var nickname: String,
    val email: String,
    val profileImage: String
)