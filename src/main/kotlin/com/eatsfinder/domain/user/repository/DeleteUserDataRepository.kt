package com.eatsfinder.domain.user.repository

import com.eatsfinder.domain.user.model.DeleteUserData
import org.springframework.data.jpa.repository.JpaRepository

interface DeleteUserDataRepository : JpaRepository<DeleteUserData, Long> {

}