package com.eatsfinder.domain.user.repository

import com.eatsfinder.domain.user.model.SocialType
import com.eatsfinder.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface UserRepository : JpaRepository<User, Long> {
    fun existsByProviderAndEmail(provider: SocialType, email: String): Boolean
    fun findByProviderAndEmail(provider: SocialType, email: String): User

    fun findByIdAndDeletedAt(id: Long, deletedAt: LocalDateTime?): User?

    fun findByEmailAndDeletedAtAndProvider(email: String, deletedAt: LocalDateTime?, provider: SocialType): User?

    fun findFirstByEmailOrNicknameAndProvider(email: String, nickname: String, provider: SocialType): User?

    fun findByNickname(nickname: String): User?

}