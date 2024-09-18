package com.eatsfinder.domain.like.repository

import com.eatsfinder.domain.like.model.PostLikes
import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PostLikeRepository : JpaRepository<PostLikes, Long> {
    fun findByUserIdAndPostId(userId: User, postId: Post): PostLikes?

    @Query("SELECT l FROM PostLikes l JOIN l.postId p WHERE p.deletedAt IS NULL AND l.userId = :userId")
    fun findByUserId(userId: User): List<PostLikes>
}