package com.eatsfinder.domain.user.model

import jakarta.persistence.*

@Entity
@Table(name = "delete_user_data")
class DeleteUserData(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val userId: User,

    @Column(name = "user_email")
    val userEmail: String,

    @Column(name = "unavailability")
    var unavailability: Boolean,

    @Column(name = "infrequent")
    var infrequent: Boolean,

    @Column(name = "privacy")
    var privacy: Boolean,

    @Column(name = "inconvenience")
    var inconvenience: Boolean,

    @Column(name = "switching")
    var switching: Boolean,

    @Column(name = "others")
    var others: Boolean,

    @Column(name = "reason")
    var reason: String? = null

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}