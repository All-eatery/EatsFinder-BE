package com.eatsfinder.domain.post.service

import com.eatsfinder.domain.follow.repository.FollowRepository
import com.eatsfinder.domain.post.dto.NewPostByNeighborResponse
import com.eatsfinder.domain.post.repository.PostRepository
import com.eatsfinder.domain.user.repository.UserRepository
import com.eatsfinder.global.exception.ModelNotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PostServiceImpl(
    private val postRepository: PostRepository,
    private val followRepository: FollowRepository,
    private val userRepository: UserRepository
): PostService {
    override fun getNewPostByNeighbor(userId: Long?): NewPostByNeighborResponse {
        val user = userRepository.findByIdAndDeletedAt(userId!!, null) ?: throw ModelNotFoundException(
            "user",
            "이 계정(id: ${userId})은 존재하지 않습니다."
        )

        val followingUser = followRepository.findByFollowedUserId(user)

        val followUserList = followingUser.map { it.followingUserId }

        val time = LocalDateTime.now().minusHours(72)

        val posts = postRepository.findPostByUserIdInAndUpdatedAtAfter(followUserList, time)

        return NewPostByNeighborResponse.from(posts ?: emptyList(), user, followingUser)

    }

}
