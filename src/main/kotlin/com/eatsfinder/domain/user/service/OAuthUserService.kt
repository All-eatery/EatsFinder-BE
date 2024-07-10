package com.eatsfinder.domain.user.service

import com.eatsfinder.domain.user.dto.oauth.OAuth2UserInfo
import com.eatsfinder.domain.user.model.SocialType
import com.eatsfinder.domain.user.model.User
import com.eatsfinder.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OAuthUserService(
    private val userRepository: UserRepository
) {
    @Transactional
    fun registerIfNotExist(oAuth2UserInfo: OAuth2UserInfo): User {
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