package com.eatsfinder.domain.post.repository

import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface PostRepository: JpaRepository<Post, Long> {

    fun findByUserId(userId: User): List<Post>?

    fun findByIdAndDeletedAt(id: Long, deletedAt: LocalDateTime?): Post?

    @Query("SELECT p FROM Post p WHERE p.userId IN :userIds AND p.updatedAt > :updatedAt ORDER BY p.updatedAt ASC")
    fun findPostByUserIdInAndOrderByUpdatedAtAfter(userIds: List<User>, updatedAt: LocalDateTime):  List<Post>?
}
