package com.eatsfinder.domain.post.repository

import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long> {

    fun findByUserId(userId: User): List<Post>?
}
