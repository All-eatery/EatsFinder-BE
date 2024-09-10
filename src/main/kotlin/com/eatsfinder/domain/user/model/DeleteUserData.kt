package com.eatsfinder.domain.user.model

import jakarta.persistence.*

@Entity
@Table(name = "delete_user_data")
class DeleteUserData(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val userId: User,

    @Column(name = "reason")
    var reason: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "reason_type", nullable = false)
    val reasonType: DeleteUserReason

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}