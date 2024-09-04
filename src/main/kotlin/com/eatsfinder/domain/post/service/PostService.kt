package com.eatsfinder.domain.post.service

import com.eatsfinder.domain.post.dto.NewPostByNeighborResponse
import org.springframework.data.domain.Pageable

interface PostService {

    fun getNewPostByNeighbor(userId: Long?, pageable: Pageable):  NewPostByNeighborResponse
}