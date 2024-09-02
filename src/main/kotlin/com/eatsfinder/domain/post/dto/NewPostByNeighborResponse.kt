package com.eatsfinder.domain.post.dto

import com.eatsfinder.domain.follow.model.Follow
import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.domain.user.model.User

data class NewPostByNeighborResponse(
    val followingCount: Int,
    val neighborPost: List<NeighborPostResponse>
){
    companion object {
        fun from(posts: List<Post>, user: User, follows: List<Follow>): NewPostByNeighborResponse {
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
            return NewPostByNeighborResponse(
                followingCount = user.followingCount,
                neighborPost = neighborPosts
            )
        }
    }
}
