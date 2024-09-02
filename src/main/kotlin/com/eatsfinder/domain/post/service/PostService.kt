package com.eatsfinder.domain.post.service

import com.eatsfinder.domain.post.dto.NewPostByNeighborResponse

interface PostService {

    fun getNewPostByNeighbor(userId: Long?): NewPostByNeighborResponse
}