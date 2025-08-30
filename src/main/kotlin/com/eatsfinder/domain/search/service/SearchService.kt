package com.eatsfinder.domain.search.service

import com.eatsfinder.domain.like.dto.PaginationPostLikeResponse
import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.domain.search.dto.NeighborSearchPaginationResponse
import com.eatsfinder.domain.search.dto.PlaceSearchPaginationResponse
import com.eatsfinder.domain.search.dto.PostSearchPaginationResponse
import com.eatsfinder.domain.search.dto.SearchResponse
import com.eatsfinder.domain.search.model.SearchFilter


interface SearchService{

    fun getSearchKeyword(keyword: String, searchFilter: SearchFilter?, postCursorId: Long?, placeCursorId: Long?, neighborCursorId: Long?, pageSize: Int): SearchResponse

    fun getPostSearchKeyword(keyword: String, postCursorId: Long?, pageSize: Int): PostSearchPaginationResponse

    fun getPlaceSearchKeyword(keyword: String,  placeCursorId: Long?, pageSize: Int): PlaceSearchPaginationResponse

    fun getNeighborSearchKeyword(keyword: String, neighborCursorId: Long?, pageSize: Int): NeighborSearchPaginationResponse


    fun getLikePostSearchKeyword(cursorId: Long?, pageSize: Int, keyword: String, userId: Long): PaginationPostLikeResponse


    fun findKeywordLog(): List<String>
}