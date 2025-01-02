package com.eatsfinder.domain.search.controller

import com.eatsfinder.domain.search.dto.SearchResponse
import com.eatsfinder.domain.search.model.SearchFilter
import com.eatsfinder.domain.search.service.SearchService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchController(
    private val searchService: SearchService
) {
    @Operation(summary = "검색하기")
    @GetMapping("/search")
    fun getSearchKeyword(
        @RequestParam keyword: String,
        @RequestParam filter: SearchFilter?
    ): ResponseEntity<SearchResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(searchService.getSearchKeyword(keyword, filter))
    }
}