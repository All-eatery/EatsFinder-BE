package com.eatsfinder.domain.contact.model

import com.eatsfinder.domain.reply.model.Reply
import com.eatsfinder.domain.user.model.User
import com.eatsfinder.global.entity.BaseTimeEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete

@Entity
@SQLDelete(sql = "UPDATE queries SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Table(name = "queries")
class Queries(

    @Column(name = "title", length = 50, nullable = false) var title: String,


    @Column(name = "content", columnDefinition = "TEXT") var content: String,

    @Column(name = "imgUrl", columnDefinition = "TEXT") var imgUrl: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: ContactStatus,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val userId: User,

    @OneToMany(mappedBy = "answerId", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var answers: MutableList<Answers> = mutableListOf(),


    ) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}