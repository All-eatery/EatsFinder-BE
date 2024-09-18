package com.eatsfinder.domain.email.model

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
@Table(name = "email_verifications")
class Email(

    @Column(name = "email", length = 30, nullable = false, unique = true)
    val email: String,

    @Column(name = "code", length = 10, nullable = false)
    var code: String


) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(name = "complete", nullable = false, columnDefinition = "TINYINT(1)")
    var complete: Boolean = false

    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP(6)", name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(columnDefinition = "TIMESTAMP(6)", name = "expired_at", nullable = false)
    var expiredAt: LocalDateTime = createdAt.plusMinutes(5)
}