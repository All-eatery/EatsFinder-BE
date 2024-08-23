package com.eatsfinder.domain.user.repository

import com.eatsfinder.domain.user.model.User
import com.eatsfinder.domain.user.model.UserLog
import org.springframework.data.jpa.repository.JpaRepository

interface UserLogRepository : JpaRepository<UserLog, Long> {
    fun findByUserId(userId: User): List<UserLog>?

}