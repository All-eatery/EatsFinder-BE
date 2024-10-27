package com.eatsfinder.domain.post.service

import com.eatsfinder.domain.post.dto.NewPostByNeighborResponse
import com.eatsfinder.domain.post.dto.TopPostResponse
import org.springframework.data.domain.Pageable

interface PostService {

    fun getNewPostByNeighbor(userId: Long?, pageable: Pageable): NewPostByNeighborResponse

    fun getTopPostList(userId: Long?): List<TopPostResponse>
}