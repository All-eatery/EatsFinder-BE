package com.eatsfinder.domain.search.controller

import com.eatsfinder.domain.like.dto.PaginationPostLikeResponse
import com.eatsfinder.domain.like.dto.PostLikesResponse
import com.eatsfinder.domain.search.dto.SearchResponse
import com.eatsfinder.domain.search.model.SearchFilter
import com.eatsfinder.domain.search.service.SearchService
import com.eatsfinder.global.security.jwt.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
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

    @Operation(summary = "좋아요한 게시물 검색하기")
    @GetMapping("/search/liked-posts")
    fun getLikePostSearchKeyword(
        @RequestParam keyword: String,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam cursorId: Long?,
        @RequestParam(defaultValue = "15") pageSize: Int
    ): ResponseEntity<PaginationPostLikeResponse> {
        val userId = userPrincipal.id
        return ResponseEntity.status(HttpStatus.OK).body(searchService.getLikePostSearchKeyword(cursorId, pageSize,keyword, userId))
    }
}