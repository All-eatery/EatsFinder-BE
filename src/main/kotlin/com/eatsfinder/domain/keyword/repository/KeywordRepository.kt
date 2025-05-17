package com.eatsfinder.domain.keyword.repository

import com.eatsfinder.domain.keyword.model.KeywordLog
import org.springframework.data.jpa.repository.JpaRepository

interface KeywordRepository: JpaRepository<KeywordLog, Long> {
    fun findByKeyword(keyword: String): KeywordLog

}