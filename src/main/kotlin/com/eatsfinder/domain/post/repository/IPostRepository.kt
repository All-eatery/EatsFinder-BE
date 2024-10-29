package com.eatsfinder.domain.post.repository

import com.eatsfinder.domain.post.dto.TopPostResponse

interface IPostRepository {

    fun getTopPostList(userId: Long?): List<TopPostResponse>
}