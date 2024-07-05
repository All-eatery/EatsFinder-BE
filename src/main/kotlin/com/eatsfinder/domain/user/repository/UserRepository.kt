package com.eatsfinder.domain.user.repository

import com.eatsfinder.domain.user.model.SocialType
import com.eatsfinder.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, Long> {
    fun existsByProviderAndEmail(provider: SocialType, email: String): Boolean
    fun findByProviderAndEmail(provider: SocialType, email: String): User

}