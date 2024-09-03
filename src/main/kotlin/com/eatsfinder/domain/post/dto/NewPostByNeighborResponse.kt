package com.eatsfinder.domain.post.dto

import com.eatsfinder.domain.follow.model.Follow
import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.domain.user.model.User
import org.springframework.data.domain.Pageable

data class NewPostByNeighborResponse(
    val pagination: PaginationNeighborPostResponse,
    val followingCount: Int,
    val neighborPost: List<NeighborPostResponse>
){
    companion object {
        fun from(posts: List<Post>, user: User, follows: List<Follow>, pageable: Pageable): NewPostByNeighborResponse {

            val neighborPosts = posts.mapNotNull { post ->
                val follow = follows.find { it.followingUserId == post.userId }
                follow?.let {
                    NeighborPostResponse(
                        followingUser = FollowingUserDataResponse(
                            nickname = it.followingUserId.nickname,
                            profileImage = it.followingUserId.profileImage,
                        ),
                        postId = post.id,
                        placeName = post.placeId.name,
                        postThumbnailUrl = post.thumbnailUrl,
                        postLikeCount = post.likeCount
                    )
                }
            }

            val totalPost = neighborPosts.size.toLong()
            val pagedPosts = neighborPosts.drop(pageable.pageNumber * pageable.pageSize)
                .take(pageable.pageSize)

            val isLastPage = (pageable.pageNumber + 1) * pageable.pageSize >= totalPost

            val pagination = PaginationNeighborPostResponse(
                page = pageable.pageNumber,
                size = pageable.pageSize,
                totalPost = totalPost,
                isLastPage = isLastPage
            )


            return NewPostByNeighborResponse(
                pagination = pagination,
                followingCount = user.followingCount,
                neighborPost = pagedPosts,
            )
        }
    }
}
