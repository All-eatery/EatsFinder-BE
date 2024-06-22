package com.eatsfinder.global.oauth.converter

import com.eatsfinder.domain.user.model.SocialType
import org.springframework.core.convert.converter.Converter
import java.security.InvalidParameterException

class OAuth2ProviderConverter : Converter<String, SocialType> {
    override fun convert(source: String): SocialType {
        return kotlin.runCatching {
            SocialType.valueOf(source.uppercase())
        }.getOrElse {
            throw InvalidParameterException("$it")
        }
    }
}