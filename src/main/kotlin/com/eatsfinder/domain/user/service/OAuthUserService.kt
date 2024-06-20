package com.eatsfinder.domain.user.service

import com.eatsfinder.domain.user.dto.OAuth2UserInfo
import com.eatsfinder.domain.user.model.SocialType
import com.eatsfinder.domain.user.model.User
import com.eatsfinder.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class OAuthUserService(
    private val userRepository: UserRepository
) {
    fun enrollIfExist(oAuth2UserInfo: OAuth2UserInfo): User {
        return if (!userRepository.existsByProviderAndProviderId(oAuth2UserInfo.provider, oAuth2UserInfo.id)) {
            val socialUser = when (oAuth2UserInfo.provider) {
                SocialType.KAKAO -> User.ofKakao(oAuth2UserInfo.id.toLong(), oAuth2UserInfo.nickname, oAuth2UserInfo.email)
                SocialType.GOOGLE -> User.ofGoogle(oAuth2UserInfo.id, oAuth2UserInfo.nickname, oAuth2UserInfo.email)
                SocialType.LOCAL -> TODO()
                SocialType.NAVER -> TODO()
            }
            userRepository.save(socialUser)
        } else {
            userRepository.findByProviderAndProviderId(oAuth2UserInfo.provider, oAuth2UserInfo.id)
        }
    }
}