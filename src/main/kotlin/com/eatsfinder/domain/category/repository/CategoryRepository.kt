package com.eatsfinder.domain.category.repository

import com.eatsfinder.domain.category.model.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, Long> {
}