package com.eatsfinder.domain.category.dto

import com.eatsfinder.domain.category.model.Category


data class CategoryResponse(
    val id: Long,
    val group: String,
    val code: String
) {
    companion object {
        fun from(category: Category): CategoryResponse {
            return CategoryResponse(
                id = category.id!!,
                group = category.group,
                code = category.code
            )
        }
    }
}
