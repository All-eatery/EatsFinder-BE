package com.eatsfinder.domain.comment.model

import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.domain.reply.model.Reply
import com.eatsfinder.domain.user.model.User
import com.eatsfinder.global.entity.BaseTimeEntity
import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.SQLDelete

@Entity
@SQLDelete(sql = "UPDATE comments SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Table(name = "comments")
class Comment(

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    var content: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val userId: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    val postId: Post,

    @ColumnDefault("0")
    @Column(name = "like_count", nullable = false)
    var likeCount: Int = 0,

    @OneToMany(mappedBy = "commentId", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val replies: MutableList<Reply> = mutableListOf(),


    ) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}