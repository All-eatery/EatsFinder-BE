package com.eatsfinder.domain.user.repository

import com.eatsfinder.domain.user.model.SocialType
import com.eatsfinder.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun existsByProviderAndProviderId(provider: SocialType, providerId: String): Boolean
    fun findByProviderAndProviderId(provider: SocialType, providerId: String): User

}