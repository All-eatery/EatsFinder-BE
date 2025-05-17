package com.eatsfinder.domain.keyword.controller

import com.eatsfinder.domain.keyword.dto.KeywordLogResponse
import com.eatsfinder.domain.keyword.service.KeywordService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/keyword")
class KeywordController(
    private val keywordService: KeywordService
) {

    @Operation(summary = "급상승 키워드")
    @GetMapping
    fun findKeyword(
        @RequestParam keyword: String
    ): ResponseEntity<List<KeywordLogResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(keywordService.findKeywordLog(keyword))
    }

}