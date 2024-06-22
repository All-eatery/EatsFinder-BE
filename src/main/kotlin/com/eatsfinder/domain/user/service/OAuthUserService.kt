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
        return if (!userRepository.existsByProviderAndEmail(oAuth2UserInfo.provider, oAuth2UserInfo.email)) {
            val socialUser = when (oAuth2UserInfo.provider) {
                SocialType.KAKAO -> User.ofKakao(oAuth2UserInfo.nickname, oAuth2UserInfo.email, oAuth2UserInfo.profileImage)
                SocialType.GOOGLE -> User.ofGoogle(oAuth2UserInfo.nickname, oAuth2UserInfo.email, oAuth2UserInfo.profileImage)
                SocialType.LOCAL -> TODO()
                SocialType.NAVER -> TODO()
            }
            userRepository.save(socialUser)
        } else {
            userRepository.findByProviderAndEmail(oAuth2UserInfo.provider, oAuth2UserInfo.email)
        }
    }
}