package com.eatsfinder.domain.user.model

import com.eatsfinder.global.entity.BaseTimeEntity
import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.SQLDelete
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@SQLDelete(sql = "UPDATE users SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Table(name = "users")
class User(

    @Column(name = "email", nullable = false, length = 30)
    val email: String,

    @Column(name = "password", columnDefinition = "TEXT", nullable = false)
    var password: String,

    @Column(name = "name", nullable = true, length = 10)
    val name: String?,

    @Column(name = "nickname", nullable = false, length = 10, unique = true)
    var nickname: String,

    @Column(name = "profile_image", columnDefinition = "TEXT")
    var profileImage: String?,

    @Column(name = "phone_number", nullable = true, length = 15)
    var phoneNumber: String,

    @ColumnDefault("0")
    @Column(name = "follower_count", nullable = false)
    var followerCount: Int = 0,

    @ColumnDefault("0")
    @Column(name = "following_count", nullable = false)
    var followingCount: Int = 0,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    val role: UserRole,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: UserStatus,

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", nullable = false)
    val provider: SocialType

) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(columnDefinition = "DATE", name = "nickname_limit_at")
    var nicknameLimitAt: LocalDate? = LocalDate.now()

    companion object{
        fun ofKakao(nickname: String, email: String, profileImage: String): User {
            return User(
                provider = SocialType.KAKAO,
                nickname = nickname,
                email = email,
                followerCount = 0,
                followingCount = 0,
                password = "",
                profileImage = profileImage,
                phoneNumber = "".replace(" ", "").replace("-", ""),
                name = "",
                status = UserStatus.NORMAL,
                role = UserRole.USER
            )
        }

        fun ofGoogle(nickname: String, email: String, profileImage: String): User {
            return User(
                provider = SocialType.GOOGLE,
                nickname = nickname,
                email = email,
                followerCount = 0,
                followingCount = 0,
                password = "",
                profileImage = profileImage,
                phoneNumber = "".replace(" ", "").replace("-", ""),
                name = "",
                status = UserStatus.NORMAL,
                role = UserRole.USER
            )
        }
    }
}