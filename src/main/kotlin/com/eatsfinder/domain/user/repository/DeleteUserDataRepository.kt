package com.eatsfinder.domain.user.repository

import com.eatsfinder.domain.user.model.UserWithdrawalData
import org.springframework.data.jpa.repository.JpaRepository

interface DeleteUserDataRepository : JpaRepository<UserWithdrawalData, Long> {

}