package com.eatsfinder.domain.search.service

import com.eatsfinder.domain.search.model.SearchFilter


interface SearchService{

    fun getSearchKeyword(userId: Long?, keyword: String, searchFilter: SearchFilter?)
}