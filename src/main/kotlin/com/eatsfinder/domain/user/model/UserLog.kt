package com.eatsfinder.domain.user.model

import com.eatsfinder.domain.comment.model.Comment
import com.eatsfinder.domain.like.model.CommentLikes
import com.eatsfinder.domain.like.model.PostLikes
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
@Table(name="user_logs")
class UserLog(

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "user_id", nullable = false)
    val userId: User,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "comment_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val commentId: Comment?,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "post_like_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val postLikeId: PostLikes?,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "comment_like_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val commentLikeId: CommentLikes?,

    @Enumerated(EnumType.STRING)
    @Column(name = "myActive_type", nullable = false)
    val myActiveType: MyActiveType

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null

    @Column(columnDefinition = "TIMESTAMP(6)", name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(columnDefinition = "TIMESTAMP(6)", name = "deleted_at", nullable = true)
    var deletedAt: LocalDateTime? = null
}