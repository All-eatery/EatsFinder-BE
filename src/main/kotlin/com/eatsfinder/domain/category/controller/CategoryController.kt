package com.eatsfinder.domain.category.controller

import com.eatsfinder.domain.category.dto.CategoryResponse
import com.eatsfinder.domain.category.service.CategoryService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/categories")
class CategoryController(
    private val categoryService: CategoryService
) {
    @Operation(summary = "카테고리(음식 종류) 전체 조회")
    @GetMapping
    fun getCategoryList(): ResponseEntity<List<CategoryResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategoryList())
    }
}