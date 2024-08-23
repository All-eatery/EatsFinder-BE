package com.eatsfinder.domain.user.dto.profile.myactive

import com.eatsfinder.domain.user.model.UserLog
import java.time.format.DateTimeFormatter


data class MyActiveResponse(
    val data: List<MyActiveDataResponse>
) {
    companion object {
        fun from(log: List<UserLog>): MyActiveResponse {
            val createdDate = DateTimeFormatter.ofPattern("yyyy.MM.dd")
            val data = log.map { logs ->
                MyActiveDataResponse(
                    type = logs.myActiveType.name,
                    postLikeId = MyActivePostLikeResponse(
                        postId = logs.postLikeId?.postId?.id,
                        createBy = MyActivePostUserResponse(
                            postUserNickname = logs.postLikeId?.postId?.userId?.nickname,
                            postUserImageUrl = logs.postLikeId?.postId?.userId?.profileImage
                        ),
                        postContent = logs.postLikeId?.postId?.content
                    ),
                    commentLikeId = MyActiveCommentLikeResponse(
                        postId = logs.commentLikeId?.commentId?.postId?.id,
                        createBy = MyActiveCommentUserResponse(
                            commentUserNickname = logs.commentLikeId?.commentId?.userId?.nickname,
                            commentUserImageUrl = logs.commentLikeId?.commentId?.userId?.profileImage
                        ),
                        commentContent = logs.commentLikeId?.commentId?.content
                    ),
                    comment = MyActiveCommentResponse(
                        id = logs.commentId?.id,
                        postId = logs.commentId?.postId?.id,
                        createBy = MyActivePostUserResponse(
                            postUserNickname = logs.commentId?.postId?.userId?.nickname,
                            postUserImageUrl = logs.commentId?.postId?.userId?.profileImage
                        ),
                        content = logs.commentId?.content
                    ),
                    createdAt = logs.createdAt.toLocalDate().format(createdDate)
                )
            }
            return MyActiveResponse(
                data = data
            )
        }
    }
}
