package com.eatsfinder.domain.post.repository

import com.eatsfinder.domain.post.model.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long> {
}