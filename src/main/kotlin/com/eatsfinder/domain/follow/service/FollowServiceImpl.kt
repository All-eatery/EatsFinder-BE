package com.eatsfinder.domain.follow.service

import com.eatsfinder.domain.follow.model.Follow
import com.eatsfinder.domain.follow.repository.FollowRepository
import com.eatsfinder.domain.user.repository.UserRepository
import com.eatsfinder.global.exception.ModelNotFoundException
import org.springframework.stereotype.Service

@Service
class FollowServiceImpl(
    private val followRepository: FollowRepository,
    private val userRepository: UserRepository
): FollowService {
    override fun createUserFollow(userId: Long, followingUserId: Long) {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException("user", "이 프로필(id:${userId})는 존재하지 않습니다.")
        val followingUser = userRepository.findByIdAndDeletedAt(followingUserId, null) ?: throw ModelNotFoundException("user", "이 프로필(id:${followingUserId})는 존재하지 않습니다.")

        val follow = followRepository.findByFollowedUserIdAndFollowingUserId(user, followingUser)

        if (follow == null){
            followRepository.save(
                Follow(
                    followingUserId = followingUser,
                    followedUserId = user
                )
            )
            user.followingCount++
            followingUser.followerCount++
            userRepository.save(user)
            userRepository.save(followingUser)
        } else {
            throw ModelNotFoundException("follow", "팔로우(${follow})는 한번 밖에 하지 못합니다.")
        }
    }

    override fun deleteUserFollow(userId: Long, unfollowingUserId: Long) {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException("user", "이 프로필(id:${userId})는 존재하지 않습니다.")
        val unfollowingUser = userRepository.findByIdAndDeletedAt(unfollowingUserId, null) ?: throw ModelNotFoundException("user", "이 프로필(id:${unfollowingUserId})는 존재하지 않습니다.")

        val follow = followRepository.findByFollowedUserIdAndFollowingUserId(user,unfollowingUser)

        if (follow != null) {
           if (user.followerCount > 0 && unfollowingUser.followingCount > 0) {
            user.followingCount--
            unfollowingUser.followerCount--
            followRepository.delete(follow)
            } else {
                throw IllegalStateException("사용자의 팔로워 수 또는 팔로잉 수가 0 이하입니다.")
            }
        } else {
            throw ModelNotFoundException("follow", "팔로우(${follow})는 한번 밖에 취소하지 못합니다.")
        }
    }
}