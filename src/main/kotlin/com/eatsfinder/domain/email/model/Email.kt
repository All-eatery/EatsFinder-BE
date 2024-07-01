package com.eatsfinder.domain.email.model

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
@Table(name = "email_verifications")
class Email(

    @Column(name = "email", length = 30, nullable = false)
    val email: String,

    @Column(name = "verification_code", length = 10, nullable = false)
    var code: String,

    @Column(name = "is_verification")
    val isVerification: Boolean

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP(6)", name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(columnDefinition = "TIMESTAMP(6)", name = "expires_at", nullable = false)
    var expiresAt: LocalDateTime? = createdAt.plusMinutes(5)
}