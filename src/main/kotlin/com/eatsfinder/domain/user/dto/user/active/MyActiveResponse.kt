package com.eatsfinder.domain.user.dto.user.active

import com.eatsfinder.global.pagination.PaginationItemsResponse
import com.eatsfinder.domain.user.model.MyActiveType
import com.eatsfinder.domain.user.model.UserLog
import org.springframework.data.domain.Pageable
import java.time.format.DateTimeFormatter


data class MyActiveResponse(
    val pagination: PaginationItemsResponse,
    val data: List<MyActiveDataResponse>
) {
    companion object {
        fun from(log: List<UserLog>, pageable: Pageable): MyActiveResponse {
            val createdDate = DateTimeFormatter.ofPattern("yyyy.MM.dd")

            val data = log.map { logs ->
                val postUserNickname = logs.postLikeId?.postId?.userId?.nickname
                val postImageUrl = logs.postLikeId?.postId?.thumbnailUrl

                val commentUserNickname = logs.commentLikeId?.commentId?.userId?.nickname
                val commentUserImageUrl = logs.commentLikeId?.commentId?.userId?.profileImage

                val replyUserNickname = logs.replyId?.userId?.nickname
                val replyImageUrl = logs.replyId?.userId?.profileImage

                when (logs.myActiveType) {
                    MyActiveType.POST_LIKES ->
                        if (logs.postLikeId?.postId?.deletedAt == null) {
                            MyActiveDataResponse(
                                type = "POST_LIKES",
                                postLike = MyActivePostLikeResponse(
                                    postId = logs.postLikeId?.postId?.id,
                                    createdBy = if (postUserNickname != null && postImageUrl != null) {
                                        MyActivePostUserResponse(
                                            postUserNickname = postUserNickname,
                                            postImageUrl = postImageUrl
                                        )
                                    } else {
                                        null
                                    },
                                    postContent = logs.postLikeId?.postId?.content
                                ),
                                commentLike = null,
                                comment = null,
                                reply = null,
                                replyLike = null,
                                createdAt = logs.createdAt.toLocalDate().format(createdDate),
                            )
                        } else {
                            null
                        }

                    MyActiveType.COMMENT -> MyActiveDataResponse(
                        type = "COMMENT",
                        postLike = null,
                        commentLike = null,
                        comment = MyActiveCommentResponse(
                            id = logs.commentId?.id,
                            postId = logs.commentId?.postId?.id,
                            postDeletedAt = logs.commentId?.postId?.deletedAt,
                            createdBy = if (postUserNickname != null && postImageUrl != null) {
                                MyActivePostUserResponse(
                                    postUserNickname = postUserNickname,
                                    postImageUrl = postImageUrl
                                )
                            } else {
                                null
                            },
                            content = logs.commentId?.content
                        ),
                        reply = null,
                        replyLike = null,
                        createdAt = logs.createdAt.toLocalDate().format(createdDate),
                    )

                    MyActiveType.COMMENT_LIKES ->
                        if (logs.commentLikeId?.commentId?.deletedAt == null) {
                            MyActiveDataResponse(
                                type = "COMMENT_LIKES",
                                postLike = null,
                                commentLike = MyActiveCommentLikeResponse(
                                    postId = logs.commentLikeId?.commentId?.postId?.id,
                                    commentId = logs.commentLikeId?.commentId?.id,
                                    createdBy = if (commentUserNickname != null && commentUserImageUrl != null) {
                                        MyActiveCommentUserResponse(
                                            commentUserNickname = commentUserNickname,
                                            commentUserImageUrl = commentUserImageUrl
                                        )
                                    } else {
                                        null
                                    },
                                    commentContent = logs.commentLikeId?.commentId?.content
                                ),
                                comment = null,
                                reply = null,
                                replyLike = null,
                                createdAt = logs.createdAt.toLocalDate().format(createdDate),
                            )
                        } else {
                            null
                        }

                    MyActiveType.REPLY ->
                        MyActiveDataResponse(
                            type = "REPLY",
                            postLike = null,
                            commentLike = null,
                            comment = null,
                            reply = MyActiveReplyResponse(
                                id = logs.replyId?.id,
                                commentId = logs.replyId?.commentId?.id,
                                commentDeletedAt = logs.replyId?.commentId?.deletedAt,
                                createdBy = if (commentUserNickname != null && commentUserImageUrl != null) {
                                    MyActiveCommentUserResponse(
                                        commentUserNickname = commentUserNickname,
                                        commentUserImageUrl = commentUserImageUrl
                                    )
                                } else {
                                    null
                                },
                                content = logs.replyId?.content
                            ),
                            replyLike = null,
                            createdAt = logs.createdAt.toLocalDate().format(createdDate),
                        )

                    MyActiveType.REPLY_LIKES ->
                        if (logs.replyLikeId?.replyId?.deletedAt == null) {
                            MyActiveDataResponse(
                                type = "REPLY_LIKES",
                                postLike = null,
                                commentLike = null,
                                comment = null,
                                reply = null,
                                replyLike = MyActiveReplyLikeResponse(
                                    replyId = logs.replyLikeId?.replyId?.id,
                                    createdBy = if (replyUserNickname != null && replyImageUrl != null) {
                                        MyActiveReplyUserResponse(
                                            replyUserNickname = replyUserNickname,
                                            replyImageUrl = replyImageUrl
                                        )
                                    } else {
                                        null
                                    },
                                    replyContent = logs.replyLikeId?.replyId?.content
                                ),
                                createdAt = logs.createdAt.toLocalDate().format(createdDate),
                            )
                        } else {
                            null
                        }
                }
            }.filterNotNull()

            val totalItems = data.size.toLong()
            val pagedItems = data.drop(pageable.pageNumber * pageable.pageSize)
                .take(pageable.pageSize)

            val totalPage = if (totalItems == 0L) {
                0L
            } else {
                (totalItems + pageable.pageSize - 1) / pageable.pageSize
            }

            val isLastPage = (pageable.pageNumber + 1) * pageable.pageSize >= totalItems

            val pagination = PaginationItemsResponse(
                totalItems = totalItems,
                itemsPerPage = pageable.pageSize,
                totalPage = totalPage,
                currentPage = pageable.pageNumber,
                isLastPage = isLastPage
            )

            return MyActiveResponse(
                pagination = pagination,
                data = pagedItems
            )
        }
    }
}