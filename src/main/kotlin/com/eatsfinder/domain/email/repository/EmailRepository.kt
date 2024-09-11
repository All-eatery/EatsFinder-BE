package com.eatsfinder.domain.email.repository

import com.eatsfinder.domain.email.model.Email
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface EmailRepository : JpaRepository<Email, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Email e WHERE e.email = :email")
    fun findByEmail(@Param("email") email: String): Email?

    fun findByCode(code: String): Email?

    fun findEmailByEmail(email: String): Email?
}