package com.eatsfinder.domain.like.model

import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.domain.user.model.User
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(name = "likes_by_posts")
class LikesByPost(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    val userId: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val postId: Post

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}