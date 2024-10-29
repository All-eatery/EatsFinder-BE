package com.eatsfinder.domain.post.controller

import com.eatsfinder.domain.post.dto.NewPostByNeighborResponse
import com.eatsfinder.domain.post.dto.TopPostResponse
import com.eatsfinder.domain.post.service.PostService
import com.eatsfinder.global.pagination.PaginationItemsResponse
import com.eatsfinder.global.security.jwt.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PostController(
    private val postService: PostService
)
{
    @Operation(summary = "이웃들의 새로운 게시글들")
    @GetMapping("/posts/follows")
    fun getNewPostByNeighbor(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
        @PageableDefault(size = 10, sort = ["updatedAt"]) pageable: Pageable,
    ): ResponseEntity<NewPostByNeighborResponse> {
        val response = if (userPrincipal == null) {
            NewPostByNeighborResponse(
                pagination = PaginationItemsResponse(totalItems = 0, itemsPerPage = 0, totalPage = 0, currentPage = 0, isLastPage = false),
                followingCount = 0,
                neighborPost = emptyList())
        } else {
            val userId = userPrincipal.id
            postService.getNewPostByNeighbor(userId, pageable)
        }

        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @Operation(summary = "인기 게시물 TOP 20")
    @GetMapping("/posts/popular")
    fun getTopPostList(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?
    ): ResponseEntity<List<TopPostResponse>> {
        val userId = userPrincipal?.id
        return ResponseEntity.status(HttpStatus.OK).body(postService.getTopPostList(userId))
    }
}