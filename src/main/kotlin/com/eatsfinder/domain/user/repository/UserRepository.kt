package com.eatsfinder.domain.user.repository

import com.eatsfinder.domain.user.model.SocialType
import com.eatsfinder.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.*

interface UserRepository : JpaRepository<User, Long> {
    fun existsByProviderAndEmail(provider: SocialType, email: String): Boolean
    fun findByProviderAndEmail(provider: SocialType, email: String): User

    fun findByIdAndDeletedAt(id: Long, deleteAt: LocalDateTime?): User?

    fun findByEmailAndDeletedAtAndProvider(email: String, deletedAt: LocalDateTime?, provider: SocialType): User?

    fun findByEmailOrNicknameAndProvider(email: String, nickname: String, provider: SocialType): User?

    fun findByNickname(nickname: String): User?

}