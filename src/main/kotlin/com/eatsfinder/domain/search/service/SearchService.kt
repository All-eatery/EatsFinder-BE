package com.eatsfinder.domain.search.service

import com.eatsfinder.domain.search.dto.LikedPostsResponse
import com.eatsfinder.domain.search.dto.SearchResponse
import com.eatsfinder.domain.search.model.SearchFilter


interface SearchService{

    fun getSearchKeyword(keyword: String, searchFilter: SearchFilter?): SearchResponse

    fun getLikePostSearchKeyword(keyword: String, userId: Long): LikedPostsResponse
}