package com.eatsfinder.domain.search.service

import com.eatsfinder.domain.post.repository.PostRepository
import com.eatsfinder.domain.search.model.SearchFilter
import com.eatsfinder.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class SearchServiceImpl(
    private val postRepository: PostRepository,
    private val placeRepository: PostRepository,
    private val userRepository: UserRepository

) : SearchService {
    override fun getSearchKeyword(userId: Long?, keyword: String, searchFilter: SearchFilter?) {
    }

}