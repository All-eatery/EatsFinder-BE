package com.eatsfinder.domain.user.model

import com.eatsfinder.global.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(

    @Column(name = "email", nullable = false, length = 30)
    val email: String,

    @Column(name = "password", columnDefinition = "TEXT", nullable = false)
    val password: String,

    @Column(name = "name", nullable = false, length = 10)
    val name: String,

    @Column(name = "nickname", nullable = false, length = 10)
    val nickname: String,

    @Column(name = "profile_image", columnDefinition = "TEXT")
    val profileImage: String,

    @Column(name = "phone_number", nullable = false, length = 11)
    val phoneNumber: String,

    @Column(name = "follow_count")
    val followCount: Int = 0,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    val role: UserRole,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: UserStatus,

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", nullable = false)
    val socialType: SocialType

) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}