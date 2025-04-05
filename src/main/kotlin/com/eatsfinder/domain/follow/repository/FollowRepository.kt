package com.eatsfinder.domain.follow.repository

import com.eatsfinder.domain.follow.model.Follow
import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.domain.user.model.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface FollowRepository: JpaRepository<Follow, Long>{

    fun findByFollowedUserIdAndFollowingUserId(followedUserId: User, followingUserId: User): Follow?

    fun findByFollowedUserId(followedUserId: User): List<Follow>

    fun findByFollowingUserId(followingUserId: User): List<Follow>

    fun findAllByFollowedUserIdAndFollowingUserIdDeletedAtIsNull(user: User, cursorId: Long, pageable: Pageable): List<Follow>

    fun findAllByFollowedUserIdAndFollowedUserIdDeletedAtIsNull(user: User, cursorId: Long, pageable: Pageable): List<Follow>

    fun countByFollowedUserId(user: User): Long

    fun countByFollowingUserId(user: User): Long

}