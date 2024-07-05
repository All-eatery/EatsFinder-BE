package com.eatsfinder.domain.comment.model

import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.domain.user.model.User
import com.eatsfinder.global.entity.BaseTimeEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete

@Entity
@SQLDelete(sql = "UPDATE comments SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Table(name = "comments")
class Comment(

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    val content: String,

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    val userId: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id", nullable = false)
    val postId: Post


) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}