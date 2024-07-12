package com.eatsfinder.domain.like.service

import com.eatsfinder.domain.like.dto.PostLikeResponse

interface PostLikeService {

    fun createPostLikes(userId: Long, postId: Long)

    fun deletePostLikes(userId: Long, postId: Long)

    fun getPostLikes(userId: Long): List<PostLikeResponse>
}