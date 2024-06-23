package com.eatsfinder.domain.category.service

import com.eatsfinder.domain.category.dto.CategoryResponse
import com.eatsfinder.domain.category.dto.CategoryResponse.Companion.from
import com.eatsfinder.domain.category.repository.CategoryRepository
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository
) : CategoryService {
    override fun getCategoryList(): List<CategoryResponse> {
        return categoryRepository.findAll().map { from(it) }
    }
}