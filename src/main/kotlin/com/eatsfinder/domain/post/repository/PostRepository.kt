package com.eatsfinder.domain.post.repository

import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface PostRepository: JpaRepository<Post, Long> {

    fun findByUserId(userId: User): List<Post>?

    fun findByIdAndDeletedAt(id: Long, deletedAt: LocalDateTime?): Post?

    fun findPostByUserIdInAndUpdatedAtAfter(userIds: List<User>, updatedAt: LocalDateTime): List<Post>?
}
