package com.eatsfinder.domain.search.controller

import com.eatsfinder.domain.like.dto.PaginationPostLikeResponse
import com.eatsfinder.domain.search.dto.NeighborSearchPaginationResponse
import com.eatsfinder.domain.search.dto.PlaceSearchPaginationResponse
import com.eatsfinder.domain.search.dto.PostSearchPaginationResponse
import com.eatsfinder.domain.search.dto.SearchResponse
import com.eatsfinder.domain.search.model.SearchFilter
import com.eatsfinder.domain.search.service.SearchService
import com.eatsfinder.global.security.jwt.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchController(
    private val searchService: SearchService
) {
    @Operation(summary = "검색하기")
    @PostMapping("/search")
    fun getSearchKeyword(
        @RequestParam keyword: String,
        @RequestParam filter: SearchFilter?,
        @RequestParam postCursorId: Long?,
        @RequestParam placeCursorId: Long?,
        @RequestParam neighborCursorId: Long?,
        @RequestParam(defaultValue = "10") pageSize: Int
    ): ResponseEntity<SearchResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(searchService.getSearchKeyword(keyword, filter, postCursorId, placeCursorId, neighborCursorId, pageSize))
    }

    @Operation(summary = "게시물 검색하기")
    @PostMapping("/search/posts")
    fun getPostSearchKeyword(
        @RequestParam keyword: String,
        @RequestParam postCursorId: Long?,
        @RequestParam(defaultValue = "10") pageSize: Int
    ): ResponseEntity<PostSearchPaginationResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(searchService.getPostSearchKeyword(keyword, postCursorId,  pageSize))
    }

    @Operation(summary = "맛집 검색하기")
    @PostMapping("/search/places")
    fun getPlaceSearchKeyword(
        @RequestParam keyword: String,
        @RequestParam placeCursorId: Long?,
        @RequestParam(defaultValue = "10") pageSize: Int
    ): ResponseEntity<PlaceSearchPaginationResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(searchService.getPlaceSearchKeyword(keyword,  placeCursorId, pageSize))
    }

    @Operation(summary = "이웃 검색하기")
    @PostMapping("/search/neighbors")
    fun getNeighborSearchKeyword(
        @RequestParam keyword: String,
        @RequestParam neighborCursorId: Long?,
        @RequestParam(defaultValue = "10") pageSize: Int
    ): ResponseEntity<NeighborSearchPaginationResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(searchService.getNeighborSearchKeyword(keyword, neighborCursorId, pageSize))
    }

    @Operation(summary = "좋아요한 게시물 검색하기")
    @GetMapping("/search/liked-posts")
    fun getLikePostSearchKeyword(
        @RequestParam keyword: String,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam cursorId: Long?,
        @RequestParam(defaultValue = "20") pageSize: Int
    ): ResponseEntity<PaginationPostLikeResponse> {
        val userId = userPrincipal.id
        return ResponseEntity.status(HttpStatus.OK).body(searchService.getLikePostSearchKeyword(cursorId, pageSize,keyword, userId))
    }

    @Operation(summary = "급상승 키워드")
    @GetMapping("/keyword")
    fun findKeyword(
    ): ResponseEntity<List<String>> {
        return ResponseEntity.status(HttpStatus.OK).body(searchService.findKeywordLog())
    }
}