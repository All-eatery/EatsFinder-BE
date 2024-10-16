package com.eatsfinder.domain.user.dto.user.active

import com.eatsfinder.domain.user.model.MyActiveType
import com.eatsfinder.domain.user.model.UserLog
import org.springframework.data.domain.Pageable
import java.time.format.DateTimeFormatter


data class MyActiveResponse(
    val pagination: PaginationActiveResponse,
    val data: List<MyActiveDataResponse>
) {
    companion object {
        fun from(log: List<UserLog>, pageable: Pageable): MyActiveResponse {
            val createdDate = DateTimeFormatter.ofPattern("yyyy.MM.dd")

            val data = log.map { logs ->
                when (logs.myActiveType) {
                    MyActiveType.POST_LIKES ->
                        if (logs.postLikeId?.postId?.deletedAt == null) {
                            MyActiveDataResponse(
                                type = "POST_LIKES",
                                postLike = MyActivePostLikeResponse(
                                    postId = logs.postLikeId?.postId?.id,
                                    createdBy = MyActivePostUserResponse(
                                        postUserNickname = logs.postLikeId?.postId?.userId?.nickname,
                                        postImageUrl = logs.postLikeId?.postId?.thumbnailUrl
                                    ),
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
                            createdBy = MyActivePostUserResponse(
                                postUserNickname = logs.commentId?.postId?.userId?.nickname,
                                postImageUrl = logs.commentId?.postId?.thumbnailUrl
                            ),
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
                                    createdBy = MyActiveCommentUserResponse(
                                        commentUserNickname = logs.commentLikeId?.commentId?.userId?.nickname,
                                        commentUserImageUrl = logs.commentLikeId?.commentId?.userId?.profileImage
                                    ),
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

                    MyActiveType.REPLY -> MyActiveDataResponse(
                        type = "REPLY",
                        postLike = null,
                        commentLike = null,
                        comment = null,
                        reply = MyActiveReplyResponse(
                            id = logs.replyId?.id,
                            commentId = logs.replyId?.commentId?.id,
                            commentDeletedAt = logs.replyId?.commentId?.deletedAt,
                            createdBy = MyActiveReplyUserResponse(
                                replyUserNickname = logs.replyId?.userId?.nickname,
                                replyImageUrl = logs.replyId?.userId?.profileImage
                            ),
                            content = logs.replyId?.content
                        ),
                        replyLike = null,
                        createdAt = logs.createdAt.toLocalDate().format(createdDate),
                    )

                    MyActiveType.REPLY_LIKES -> TODO()
                }
            }.filterNotNull()

            val totalActive = data.size.toLong()
            val pagedActives = data.drop(pageable.pageNumber * pageable.pageSize)
                .take(pageable.pageSize)

            val totalPage = if (totalActive == 0L) {
                0L
            } else {
                (totalActive + pageable.pageSize - 1) / pageable.pageSize
            }

            val isLastPage = (pageable.pageNumber + 1) * pageable.pageSize >= totalActive

            val pagination = PaginationActiveResponse(
                totalActive = totalActive,
                activesPerPage = pageable.pageSize,
                totalPage = totalPage,
                currentPage = pageable.pageNumber,
                isLastPage = isLastPage
            )

            return MyActiveResponse(
                pagination = pagination,
                data = pagedActives
            )
        }
    }
}