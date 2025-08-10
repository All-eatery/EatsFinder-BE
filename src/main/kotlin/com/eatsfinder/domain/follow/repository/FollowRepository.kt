package com.eatsfinder.domain.follow.repository

import com.eatsfinder.domain.follow.model.Follow
import com.eatsfinder.domain.user.model.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface FollowRepository: JpaRepository<Follow, Long> {

    fun findByFollowedUserIdAndFollowingUserId(followedUserId: User, followingUserId: User): Follow?

    fun findByFollowedUserId(followedUserId: User): List<Follow>

    fun findByFollowingUserId(followingUserId: User): List<Follow>

    @Query("""
    select case when count(f) > 0 then true else false end
    from Follow f
    where f.followedUserId.id = :followedUserId
      and f.followingUserId.id = :followingUserId
""")
    fun existsByFollowedUserIdAndFollowingUserId(
        @Param("followedUserId") followedUserId: Long?,
        @Param("followingUserId") followingUserId: Long
    ): Boolean

    fun findAllByFollowedUserIdAndIdGreaterThanOrderByIdAsc(user: User, cursorId: Long, pageable: Pageable): List<Follow>

    fun findAllByFollowedUserIdOrderByIdAsc(user: User, pageable: Pageable): List<Follow>

    fun findAllByFollowingUserIdAndIdGreaterThanOrderByIdAsc(user: User, cursorId: Long, pageable: Pageable): List<Follow>

    fun findByFollowingUserIdOrderByIdAsc(user: User, pageable: Pageable): List<Follow>

    fun countByFollowedUserId(user: User): Long

    fun countByFollowingUserId(user: User): Long

}