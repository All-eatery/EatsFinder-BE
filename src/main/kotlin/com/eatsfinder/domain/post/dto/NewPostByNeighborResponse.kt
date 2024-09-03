package com.eatsfinder.domain.post.dto

import com.eatsfinder.domain.follow.model.Follow
import com.eatsfinder.domain.like.model.PostLikes
import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.domain.user.model.User
import org.springframework.data.domain.Pageable

data class NewPostByNeighborResponse(
    val pagination: PaginationNeighborPostResponse,
    val followingCount: Int,
    val neighborPost: List<NeighborPostResponse>
){
    companion object {
        fun from(posts: List<Post>, user: User, follows: List<Follow>,  likes: List<PostLikes>, pageable: Pageable): NewPostByNeighborResponse {

            val neighborPosts = posts.mapNotNull { post ->
                val follow = follows.find { it.followingUserId == post.userId }
                follow?.let {
                    val isPostLike = likes.any { like -> like.postId.id == post.id && like.userId.id == user.id }
                    NeighborPostResponse(
                        followingUser = FollowingUserDataResponse(
                            nickname = it.followingUserId.nickname,
                            profileImage = it.followingUserId.profileImage,
                        ),
                        postId = post.id,
                        placeName = post.placeId.name,
                        postThumbnailUrl = post.thumbnailUrl,
                        isPostLike = isPostLike,
                        postLikeCount = post.likeCount,
                        updatedAt = post.updatedAt
                    )
                }
            }

            val totalPost = neighborPosts.size.toLong()
            val pagedPosts = neighborPosts.drop(pageable.pageNumber * pageable.pageSize)
                .take(pageable.pageSize)

            val totalPage = if (totalPost == 0L) {
                0L
            } else {
                (totalPost + pageable.pageSize - 1) / pageable.pageSize
            }

            val isLastPage = (pageable.pageNumber + 1) * pageable.pageSize >= totalPost

            val pagination = PaginationNeighborPostResponse(
                totalPosts = totalPost,
                postsPerPage = pageable.pageSize,
                totalPage = totalPage,
                currentPage = pageable.pageNumber,
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
