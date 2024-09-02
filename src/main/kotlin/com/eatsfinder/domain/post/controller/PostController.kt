package com.eatsfinder.domain.post.controller

import com.eatsfinder.domain.post.dto.NewPostByNeighborResponse
import com.eatsfinder.domain.post.service.PostService
import com.eatsfinder.global.security.jwt.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
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
    @Operation(summary = "이웃의 새로운 게시글")
    @GetMapping("/neighbor/new-post")
    fun getNewPostByNeighbor(@AuthenticationPrincipal userPrincipal: UserPrincipal?): ResponseEntity<NewPostByNeighborResponse> {
        val response = if (userPrincipal == null) {
            NewPostByNeighborResponse(followingCount = 0, neighborPost = emptyList())
        } else {
            val userId = userPrincipal.id
            postService.getNewPostByNeighbor(userId)
        }

        return ResponseEntity.status(HttpStatus.OK).body(response)
    }
}