package com.eatsfinder.domain.search.service

import com.eatsfinder.domain.search.dto.SearchResponse
import com.eatsfinder.domain.search.model.SearchFilter
import org.springframework.data.domain.Pageable


interface SearchService{

    fun getSearchKeyword(keyword: String, searchFilter: SearchFilter?): SearchResponse
}