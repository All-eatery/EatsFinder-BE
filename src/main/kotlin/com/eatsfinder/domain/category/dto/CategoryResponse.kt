package com.eatsfinder.domain.category.dto

import com.eatsfinder.domain.category.model.Category


data class CategoryResponse(
    val id: Long,
    val type: String,
    val code: String
) {
    companion object {
        fun from(category: Category): CategoryResponse {
            return CategoryResponse(
                id = category.id!!,
                type = category.type,
                code = category.code
            )
        }
    }
}
