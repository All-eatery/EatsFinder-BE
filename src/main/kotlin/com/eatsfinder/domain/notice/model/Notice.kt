package com.eatsfinder.domain.notice.model

import com.eatsfinder.domain.user.model.User
import com.eatsfinder.global.entity.BaseTimeEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete

@Entity
@SQLDelete(sql = "UPDATE notices SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Table(name = "notices")
class Notice(

    @Column(name = "title", length = 50, nullable = false) var title: String,


    @Column(name = "content", columnDefinition = "TEXT") var content: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val userId: User

) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}