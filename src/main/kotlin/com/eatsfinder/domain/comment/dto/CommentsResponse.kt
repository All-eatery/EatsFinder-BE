package com.eatsfinder.domain.comment.dto

import com.eatsfinder.domain.comment.model.Comment
import com.eatsfinder.global.security.jwt.UserPrincipal

data class CommentsResponse(
    val totalCommentCount: Int = 0,
    val comments: List<CommentResponse>
) {
    companion object {
        fun from(comments: List<Comment>, userPrincipal: UserPrincipal?, commentCount: Int): CommentsResponse {
            val res = comments.map { comment ->
                CommentResponse(
                    id = comment.id!!,
                    nickname = comment.userId.nickname,
                    profileImage = comment.userId.profileImage,
                    content = comment.content,
                    likeCount = comment.likeCount,
                    isMyComment = (userPrincipal != null && comment.userId.id == userPrincipal.id),
                    createdAt = comment.createdAt
                )
            }
            return CommentsResponse(
                totalCommentCount = commentCount,
                comments = res
            )
        }
    }
}
