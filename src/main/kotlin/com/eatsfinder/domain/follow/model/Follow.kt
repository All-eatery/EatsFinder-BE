package com.eatsfinder.domain.follow.model

import com.eatsfinder.domain.user.model.User
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
@Table(name = "follows")
class Follow(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_users_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val followedUserId: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_users_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val followingUserId: User
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
}