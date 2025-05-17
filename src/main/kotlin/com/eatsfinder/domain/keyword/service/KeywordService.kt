package com.eatsfinder.domain.keyword.service

import com.eatsfinder.domain.keyword.dto.KeywordLogResponse

interface KeywordService{
    fun findKeywordLog(keyword: String): List<KeywordLogResponse>
}