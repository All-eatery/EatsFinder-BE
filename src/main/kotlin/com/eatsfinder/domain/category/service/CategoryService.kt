package com.eatsfinder.domain.category.service

import com.eatsfinder.domain.category.dto.CategoryResponse

interface CategoryService {
    fun getCategoryList(): List<CategoryResponse>
}