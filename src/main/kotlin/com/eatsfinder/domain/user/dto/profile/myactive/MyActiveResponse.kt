package com.eatsfinder.domain.user.dto.profile.myactive

import com.eatsfinder.domain.user.model.MyActiveType
import com.eatsfinder.domain.user.model.UserLog
import java.time.format.DateTimeFormatter


data class MyActiveResponse(
    val data: List<MyActiveDataResponse>
) {
    companion object {
        fun from(log: List<UserLog>): MyActiveResponse {
            val createdDate = DateTimeFormatter.ofPattern("yyyy.MM.dd")
            val data = log.map { logs ->
                when (logs.myActiveType) {
                    MyActiveType.POST_LIKES -> MyActiveDataResponse(
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
                        createdAt = logs.createdAt.toLocalDate().format(createdDate)
                    )
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
                        createdAt = logs.createdAt.toLocalDate().format(createdDate)
                    )
                    MyActiveType.COMMENT_LIKES -> MyActiveDataResponse(
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
                        createdAt = logs.createdAt.toLocalDate().format(createdDate)
                    )
                }
            }
            return MyActiveResponse(data = data)
        }
    }
}