package com.eatsfinder.domain.comment.dto

import com.eatsfinder.domain.comment.model.Comment
import com.eatsfinder.domain.like.model.CommentLikes
import com.eatsfinder.domain.like.model.ReplyLikes
import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.domain.reply.dto.ReplyResponse
import com.eatsfinder.global.pagination.PaginationItemsResponse
import com.eatsfinder.global.security.jwt.UserPrincipal

data class CommentsResponse(
    val pagination: PaginationItemsResponse?,
    val items: List<CommentResponse>,
    val lastItemId: Long?
) {
    companion object {
        fun from(totalCount: Long, pageSize: Int, comments: List<Comment>, userPrincipal: UserPrincipal?, commentLikes: List<CommentLikes>?, replyLikes: List<ReplyLikes>?, post: Post): CommentsResponse {
            val res = comments.map { comment ->
                CommentResponse(
                    id = comment.id!!,
                    nickname = comment.userId.nickname,
                    profileImage = comment.userId.profileImage,
                    content = comment.content,
                    likeCount = comment.likeCount,
                    isMyComment = (userPrincipal != null && comment.userId.id == userPrincipal.id),
                    likeStatus = (commentLikes?.any { it.commentId.id == comment.id && it.userId.id == userPrincipal?.id } == true),
                    authorStatus = (post.userId.id == comment.userId.id),
                    createdAt = comment.createdAt,
                    isUpdated = (comment.updatedAt != comment.createdAt),
                    totalReplyCount = comment.replies.size,
                    replies = comment.replies.map { reply ->
                        ReplyResponse(
                            id = reply.id!!,
                            nickname = reply.userId.nickname,
                            profileImage = reply.userId.profileImage,
                            content = reply.content,
                            likeCount = reply.likeCount,
                            isMyComment = (userPrincipal != null && reply.userId.id == userPrincipal.id),
                            likeStatus = (replyLikes?.any { it.replyId.id == reply.id && it.userId.id == userPrincipal?.id } == true),
                            authorStatus = (post.userId.id == reply.userId.id),
                            createdAt = reply.createdAt,
                            isUpdated = (reply.updatedAt != reply.createdAt)
                        )
                    }
                )
            }
            val isLastPage = comments.size <= pageSize

            val nextCursorId = when {
                comments.isEmpty() -> null
                comments.size > pageSize -> comments[pageSize - 1].id
                else -> comments.last().id
            }

            val pagination = PaginationItemsResponse(
                totalItems = totalCount,
                itemsPerPage = pageSize,
                totalPage = (comments.size / pageSize).toLong() + if (comments.size % pageSize > 0) 1 else 0,
                currentPage = 1,
                isLastPage = isLastPage
            )


            return CommentsResponse(
                pagination = pagination ,
                items = res.take(pageSize),
                lastItemId =  nextCursorId
            )
        }
    }
}
