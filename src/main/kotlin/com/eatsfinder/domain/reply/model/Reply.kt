package com.eatsfinder.domain.reply.model

import com.eatsfinder.domain.comment.model.Comment
import com.eatsfinder.domain.user.model.User
import com.eatsfinder.global.entity.BaseTimeEntity
import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault

@Entity
@Table(name = "replies")
class Reply(

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    var content: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val userId: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    val commentId: Comment,

    @ColumnDefault("0")
    @Column(name = "like_count", nullable = false)
    var likeCount: Int = 0

) : BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}