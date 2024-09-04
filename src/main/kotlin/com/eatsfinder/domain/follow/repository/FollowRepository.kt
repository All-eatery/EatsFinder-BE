package com.eatsfinder.domain.follow.repository

import com.eatsfinder.domain.follow.model.Follow
import com.eatsfinder.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface FollowRepository: JpaRepository<Follow, Long> {

    fun findByFollowedUserIdAndFollowingUserId(followedUserId: User, followingUserId: User): Follow?

    fun findByFollowedUserId(followedUserId: User): List<Follow>
}