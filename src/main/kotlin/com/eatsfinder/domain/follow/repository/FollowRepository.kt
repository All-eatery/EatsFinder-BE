package com.eatsfinder.domain.follow.repository

import com.eatsfinder.domain.follow.model.Follow
import com.eatsfinder.domain.user.model.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface FollowRepository: JpaRepository<Follow, Long>{

    fun findByFollowedUserIdAndFollowingUserId(followedUserId: User, followingUserId: User): Follow?

    fun findByFollowedUserId(followedUserId: User): List<Follow>

    fun findByFollowingUserId(followingUserId: User): List<Follow>

    fun findAllByFollowedUserIdAndFollowingUserIdDeletedAtIsNull(user: User, pageable: Pageable): List<Follow>

    fun findByIdLessThanOrderByIdDescCreatedAtDesc(
        @Param("cursorId") cursorId: Long,
        pageable: Pageable
    ): List<Follow>
}