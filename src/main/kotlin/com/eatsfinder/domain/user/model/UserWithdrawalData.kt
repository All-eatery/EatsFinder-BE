package com.eatsfinder.domain.user.model

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault

@Entity
@Table(name = "user_withdrawal_data")
class UserWithdrawalData(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val userId: User,

    @Column(name = "user_email")
    val userEmail: String,

    @Column(name = "user_nickname")
    val userNickname: String,

    @ColumnDefault("false")
    @Column(name = "unavailability")
    var unavailability: Boolean = false,

    @ColumnDefault("false")
    @Column(name = "infrequent")
    var infrequent: Boolean = false,

    @ColumnDefault("false")
    @Column(name = "privacy")
    var privacy: Boolean = false,

    @ColumnDefault("false")
    @Column(name = "inconvenience")
    var inconvenience: Boolean = false,

    @ColumnDefault("false")
    @Column(name = "switching")
    var switching: Boolean = false,

    @ColumnDefault("false")
    @Column(name = "others")
    var others: Boolean = false,

    @Column(name = "reason")
    var reason: String? = null

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}