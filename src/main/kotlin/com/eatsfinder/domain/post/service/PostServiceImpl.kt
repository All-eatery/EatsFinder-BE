package com.eatsfinder.domain.post.service

import com.eatsfinder.domain.follow.repository.FollowRepository
import com.eatsfinder.domain.like.repository.PostLikeRepository
import com.eatsfinder.domain.post.dto.NewPostByNeighborResponse
import com.eatsfinder.domain.post.repository.PostRepository
import com.eatsfinder.domain.report.repository.ReportPostRepository
import com.eatsfinder.domain.user.repository.UserRepository
import com.eatsfinder.global.exception.ModelNotFoundException
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PostServiceImpl(
    private val postRepository: PostRepository,
    private val postLikeRepository: PostLikeRepository,
    private val followRepository: FollowRepository,
    private val userRepository: UserRepository,
    private val reportPostRepository: ReportPostRepository
): PostService {
    override fun getNewPostByNeighbor(userId: Long?, pageable: Pageable): NewPostByNeighborResponse {
        val user = userRepository.findByIdAndDeletedAt(userId!!, null) ?: throw ModelNotFoundException(
            "user",
            "이 계정(id: ${userId})은 존재하지 않습니다."
        )

        val followUser = followRepository.findByFollowedUserId(user)

        val followingUserList = followUser.map { it.followingUserId }

        val time = LocalDateTime.now().minusHours(72)

        val posts = postRepository.findPostByUserIdInAndOrderByUpdatedAtAfter(followingUserList, time)?.filterNot {
            reportPostRepository.existsByPostIdAndUserId(it, user)
        }

        val postLikes = postLikeRepository.findByUserId(user)

        return NewPostByNeighborResponse.from(posts ?: emptyList(), user, followUser, postLikes, pageable)

    }

}
