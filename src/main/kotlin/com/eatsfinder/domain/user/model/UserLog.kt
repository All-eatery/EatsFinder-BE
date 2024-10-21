package com.eatsfinder.domain.user.model

import com.eatsfinder.domain.comment.model.Comment
import com.eatsfinder.domain.like.model.CommentLikes
import com.eatsfinder.domain.like.model.PostLikes
import com.eatsfinder.domain.like.model.ReplyLikes
import com.eatsfinder.domain.reply.model.Reply
import jakarta.persistence.*
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
    val commentId: Comment?,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "post_like_id", nullable = true)
    val postLikeId: PostLikes?,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "comment_like_id", nullable = true)
    val commentLikeId: CommentLikes?,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "reply_id", nullable = true)
    val replyId: Reply?,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "reply_like_id", nullable = true)
    val replyLikeId: ReplyLikes?,

    @Enumerated(EnumType.STRING)
    @Column(name = "myActive_type", nullable = false, length = 20)
    val myActiveType: MyActiveType

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null

    @Column(columnDefinition = "TIMESTAMP(6)", name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    var createdAt: LocalDateTime = LocalDateTime.now()
}