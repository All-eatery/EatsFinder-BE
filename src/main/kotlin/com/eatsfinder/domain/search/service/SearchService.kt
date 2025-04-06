package com.eatsfinder.domain.search.service

import com.eatsfinder.domain.like.dto.PaginationPostLikeResponse
import com.eatsfinder.domain.search.dto.SearchResponse
import com.eatsfinder.domain.search.model.SearchFilter


interface SearchService{

    fun getSearchKeyword(keyword: String, searchFilter: SearchFilter?, postCursorId: Long?, placeCursorId: Long?, neighborCursorId: Long?, pageSize: Int): SearchResponse

    fun getLikePostSearchKeyword(cursorId: Long?, pageSize: Int, keyword: String, userId: Long): PaginationPostLikeResponse
}