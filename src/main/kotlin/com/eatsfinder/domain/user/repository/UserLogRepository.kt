package com.eatsfinder.domain.user.repository

import com.eatsfinder.domain.comment.model.Comment
import com.eatsfinder.domain.like.model.CommentLikes
import com.eatsfinder.domain.like.model.PostLikes
import com.eatsfinder.domain.like.model.ReplyLikes
import com.eatsfinder.domain.user.model.MyActiveType
import com.eatsfinder.domain.user.model.User
import com.eatsfinder.domain.user.model.UserLog
import org.springframework.data.jpa.repository.JpaRepository

interface UserLogRepository : JpaRepository<UserLog, Long> {
    fun findByUserId(userId: User): List<UserLog>?

    fun findUserLogByPostLikeIdAndMyActiveType(postLikeId: PostLikes, myActiveType: MyActiveType) : UserLog?

    fun findUserLogByCommentLikeIdAndMyActiveType(commentLikeId: CommentLikes ,myActiveType: MyActiveType) : UserLog?

    fun findUserLogByCommentIdAndMyActiveType(commentId: Comment, myActiveType: MyActiveType) : UserLog?

    fun findUserLogByReplyLikeIdAndMyActiveType(replyLikeId: ReplyLikes, myActiveType: MyActiveType): UserLog?

}