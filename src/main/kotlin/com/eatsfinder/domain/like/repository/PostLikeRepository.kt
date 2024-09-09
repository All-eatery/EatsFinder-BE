package com.eatsfinder.domain.like.repository

import com.eatsfinder.domain.like.model.PostLikes
import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface PostLikeRepository : JpaRepository<PostLikes, Long> {
    fun findByUserIdAndPostId(userId: User, postId: Post): PostLikes?
    fun findByUserId(userId: User): List<PostLikes>
}