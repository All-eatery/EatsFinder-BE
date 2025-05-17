package com.eatsfinder.domain.keyword.service

import com.eatsfinder.domain.keyword.dto.KeywordLogResponse
import com.eatsfinder.domain.keyword.repository.KeywordRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class KeywordServiceImpl(
    private val redisTemplate: RedisTemplate<String, KeywordLogResponse>,
    private val keywordRepository: KeywordRepository
): KeywordService {

    override fun findKeywordLog(keyword: String): List<KeywordLogResponse> {
        val searchKeyword = keywordRepository.findByKeyword(keyword)
        val key = "$searchKeyword"
        val logs: MutableSet<KeywordLogResponse>? = redisTemplate.opsForZSet().reverseRange(key, 0, 6)

        val objectMapper = ObjectMapper()
        objectMapper.writeValueAsString(KeywordLogResponse)

        return logs!!.map { it }
    }
}


