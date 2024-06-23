package com.eatsfinder.domain.category.dto

import com.eatsfinder.domain.category.model.Category


data class CategoryResponse(
    val id: Long,
    val classification: String
) {
    companion object {
        fun from(category: Category): CategoryResponse {
            return CategoryResponse(
                id = category.id!!,
                classification = category.classification
            )
        }
    }
}
