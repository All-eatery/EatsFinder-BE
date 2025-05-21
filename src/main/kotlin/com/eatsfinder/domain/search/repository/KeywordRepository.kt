package com.eatsfinder.domain.search.repository

import com.eatsfinder.domain.search.model.KeywordLog
import org.springframework.data.jpa.repository.JpaRepository

interface KeywordRepository: JpaRepository<KeywordLog, Long> {
    fun findByKeyword(keyword: String): KeywordLog?
}