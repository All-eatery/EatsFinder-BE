package com.eatsfinder.domain.follow.service

import com.eatsfinder.domain.follow.dto.*
import com.eatsfinder.domain.follow.model.Follow
import com.eatsfinder.domain.follow.repository.FollowRepository
import com.eatsfinder.domain.user.repository.UserRepository
import com.eatsfinder.global.exception.InvalidInputException
import com.eatsfinder.global.exception.ModelNotFoundException
import com.eatsfinder.global.exception.like.DefaultZeroException
import com.eatsfinder.global.pagination.PaginationCursorItemsResponse
import com.eatsfinder.global.pagination.PaginationItemsResponse
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FollowServiceImpl(
    private val followRepository: FollowRepository,
    private val userRepository: UserRepository
) : FollowService {


    @Transactional(readOnly = true)
    override fun checkFollowing(userId: Long, followUserId: Long): FollowResponse {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필(id:${userId})는 존재하지 않습니다."
        )
        val followingUser = userRepository.findByIdAndDeletedAt(followUserId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필(id:${followUserId})는 존재하지 않습니다."
        )

        val follow = followRepository.findByFollowedUserIdAndFollowingUserId(user, followingUser)
            ?: throw ModelNotFoundException(
                "follow",
                "팔로우 한 계정이 아닙니다."
            )

        return FollowResponse.from(follow)
    }

    override fun createUserFollow(userId: Long, followUserId: Long) {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필(id:${userId})는 존재하지 않습니다."
        )
        val followUser = userRepository.findByIdAndDeletedAt(followUserId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필(id:${followUserId})는 존재하지 않습니다."
        )

        val follow = followRepository.findByFollowedUserIdAndFollowingUserId(user, followUser)

        if (user.followerCount < 0 && followUser.followingCount < 0) throw DefaultZeroException("사용자의 팔로워 수 또는 팔로잉 수가 0 이하입니다.")

        if (userId == followUserId) throw InvalidInputException("본인에게 팔로우를 할 수는 없습니다.")

        if (follow == null) {
            followRepository.save(
                Follow(
                    followingUserId = followUser,
                    followedUserId = user
                )
            )
            user.followingCount++
            followUser.followerCount++
            userRepository.save(user)
            userRepository.save(followUser)
        } else {
            throw ModelNotFoundException("follow", "이미 팔로우(${follow})중입니다.")
        }
    }

    @Transactional
    override fun deleteUserFollow(userId: Long, followUserId: Long) {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필(id:${userId})는 존재하지 않습니다."
        )
        val unfollowUser =
            userRepository.findByIdAndDeletedAt(followUserId, null) ?: throw ModelNotFoundException(
                "user",
                "이 프로필(id:${followUserId})는 존재하지 않습니다."
            )

        val follow = followRepository.findByFollowedUserIdAndFollowingUserId(user, unfollowUser)

        if (user.followerCount < 0 && unfollowUser.followingCount < 0) throw DefaultZeroException("사용자의 팔로워 수 또는 팔로잉 수가 0 이하입니다.")

        if (userId == followUserId) throw InvalidInputException("본인에게 언팔로우를 할 수 없습니다.")

        if (follow != null) {
            followRepository.delete(follow)
            user.followingCount--
            unfollowUser.followerCount--
            userRepository.save(user)
            userRepository.save(unfollowUser)
        } else {
            throw ModelNotFoundException("follow", "이미 언팔로우하셨습니다.")
        }
    }

    @Transactional(readOnly = true)
    override fun findFollowingListCursorBased(
        cursorId: Long?,
        pageSize: Int,
        userId: Long
    ): PaginationFollowingResponse {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필(id:${userId})는 존재하지 않습니다."
        )

        val pageable = PageRequest.of(0, pageSize + 1)

        val follows = if (cursorId == null) {
            followRepository.findByFollowedUserId(user)
        } else {
            followRepository.findAllByFollowedUserIdAndFollowedUserIdDeletedAtIsNull(user, cursorId, pageable)
        }

        val followList: List<FollowerListResponse> = follows.map { follow ->
            FollowerListResponse(
                followerUserId = follow.followingUserId.id!!,
                followerUserNickname = follow.followingUserId.nickname,
                imageUrl = follow.followingUserId.profileImage
            )
        }
        val isLastPage = followList.size <= pageSize
        val nextCursorId = if (followList.size <= pageSize) followList.last().followerUserId else null

        val totalCount = followRepository.countByFollowedUserId(user)


        val pagination = PaginationItemsResponse(
            totalItems = totalCount,
            itemsPerPage = pageSize,
            totalPage = (follows.size / pageSize).toLong() + if (follows.size % pageSize > 0) 1 else 0,
            currentPage = 1,
            isLastPage = isLastPage
        )

        return PaginationFollowingResponse(
            pagination = pagination,
            items = followList.take(pageSize),
            lastItemId = nextCursorId
        )
    }

    @Transactional(readOnly = true)
    override fun findFollowerListCursorBased(cursorId: Long?, pageSize: Int, userId: Long): PaginationFollowerResponse {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException(
            "user",
            "이 프로필(id:${userId})는 존재하지 않습니다."
        )

        val pageable = PageRequest.of(0, pageSize + 1)

        val follows = if (cursorId == null) {
            followRepository.findByFollowingUserId(user)
        }else {
            followRepository.findAllByFollowedUserIdAndFollowingUserIdDeletedAtIsNull(user, cursorId, pageable)
        }

        val followList: List<FollowingListResponse> = follows.map { follow ->
            FollowingListResponse(
                followingUserId = follow.followedUserId.id!!,
                followingUserNickname = follow.followedUserId.nickname,
                imageUrl = follow.followedUserId.profileImage
            )
        }

        val isLastPage = followList.size <= pageSize
        val nextCursorId = when {
            followList.isEmpty() -> null
            followList.size > pageSize -> followList[pageSize - 1].followingUserId
            else -> followList.last().followingUserId
        }

        val totalCount = followRepository.countByFollowingUserId(user)

        val pagination = PaginationItemsResponse(
            totalItems = totalCount,
            itemsPerPage = pageSize,
            totalPage = (follows.size / pageSize).toLong() + if (follows.size % pageSize > 0) 1 else 0,
            currentPage = 1,
            isLastPage = isLastPage
        )

        return PaginationFollowerResponse(
            pagination = pagination,
            items = followList.take(pageSize),
            lastItemId = nextCursorId
        )
    }

}