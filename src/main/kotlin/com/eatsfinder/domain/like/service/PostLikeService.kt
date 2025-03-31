package com.eatsfinder.domain.like.service

import com.eatsfinder.domain.like.dto.PaginationPostLikeResponse

interface PostLikeService {

    fun createPostLikes(userId: Long, postId: Long)

    fun deletePostLikes(userId: Long, postId: Long)

    fun getPostLikes(userId: Long, cursorId: Long?, pageSize: Int): PaginationPostLikeResponse
}