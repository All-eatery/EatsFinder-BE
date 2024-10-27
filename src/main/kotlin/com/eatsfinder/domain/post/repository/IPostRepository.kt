package com.eatsfinder.domain.post.repository

import com.eatsfinder.domain.post.dto.TopPostResponse

interface IPostRepository {

    fun getTopPost(): List<TopPostResponse>
}