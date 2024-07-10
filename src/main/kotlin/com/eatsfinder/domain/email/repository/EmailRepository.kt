package com.eatsfinder.domain.email.repository

import com.eatsfinder.domain.email.model.Email
import org.springframework.data.jpa.repository.JpaRepository

interface EmailRepository : JpaRepository<Email, Long> {
    fun findByEmail(email: String): Email?

    fun findByCode(code: String): Email?

    fun findByCodeAndComplete(code: String, complete: Boolean): Email?
}