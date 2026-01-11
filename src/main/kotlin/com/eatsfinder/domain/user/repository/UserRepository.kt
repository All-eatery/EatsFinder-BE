package com.eatsfinder.domain.user.repository

import com.eatsfinder.domain.user.model.SocialType
import com.eatsfinder.domain.user.model.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface UserRepository : JpaRepository<User, Long> {
    fun existsByProviderAndEmail(provider: SocialType, email: String): Boolean
    fun findByProviderAndEmail(provider: SocialType, email: String): User

    fun findByIdAndDeletedAt(id: Long, deletedAt: LocalDateTime?): User?

    fun findUserByIdAndDeletedAt(id: Long?, deletedAt: LocalDateTime?): User?

    fun findByEmailAndDeletedAtAndProvider(email: String, deletedAt: LocalDateTime?, provider: SocialType): User?

    fun findFirstByEmailOrNicknameAndProvider(email: String, nickname: String, provider: SocialType): User?

    fun findByNickname(nickname: String): User?

    fun findAllByNicknameContaining(nickname: String, pageable: Pageable): List<User>
    fun countByNicknameContaining(nickname: String): Long

    @Query("SELECT u FROM User u WHERE (u.nickname LIKE %:keyword% OR u.email LIKE %:keyword%) AND (u.id > :neighborCursorId OR :neighborCursorId IS NULL) AND u.deletedAt IS NULL ORDER BY u.id ASC")
    fun findAllByKeywordsAndIdGreaterThan(
        @Param("keyword") keyword: String,
        @Param("neighborCursorId") neighborCursorId: Long?,
        pageable: Pageable
    ): List<User>

}