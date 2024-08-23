package com.eatsfinder.domain.user.dto.profile.myactive

import com.eatsfinder.domain.user.model.UserLog
import java.time.format.DateTimeFormatter


data class MyActiveResponse(
    val user: MyActiveUserResponse,
    val data: List<MyActiveDataResponse>
) {
    companion object {
        fun from(log: List<UserLog>): MyActiveResponse {
            val createdDate = DateTimeFormatter.ofPattern("yyyy.MM.dd")
            val user = MyActiveUserResponse(
                userNickname = log.first().userId.nickname,
                userImageUrl = log.first().userId.profileImage
            )
            val data = log.map { logs ->
                MyActiveDataResponse(
                    postLikeId = logs.postLikeId?.id,
                    commentLikeId = logs.commentLikeId?.id,
                    comment = MyActiveCommentResponse(
                        id = logs.commentId?.id,
                        content = logs.commentId?.content
                    ),
                    createdAt = logs.createdAt.toLocalDate().format(createdDate)
                )
            }
            return MyActiveResponse(
                user = user,
                data = data
            )
        }
    }
}
