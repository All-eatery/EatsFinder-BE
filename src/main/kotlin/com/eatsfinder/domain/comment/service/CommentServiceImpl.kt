package com.eatsfinder.domain.comment.service

import com.eatsfinder.domain.comment.dto.CommentRequest
import com.eatsfinder.domain.comment.dto.CommentResponse
import com.eatsfinder.domain.comment.dto.CommentResponse.Companion.from
import com.eatsfinder.domain.comment.model.Comment
import com.eatsfinder.domain.comment.repository.CommentRepository
import com.eatsfinder.domain.like.model.CommentLikes
import com.eatsfinder.domain.post.repository.PostRepository
import com.eatsfinder.domain.user.model.MyActiveType
import com.eatsfinder.domain.user.model.UserLog
import com.eatsfinder.domain.user.repository.UserLogRepository
import com.eatsfinder.domain.user.repository.UserRepository
import com.eatsfinder.global.exception.ModelNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentServiceImpl(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val userLogRepository: UserLogRepository
) : CommentService {

    @Transactional(readOnly = true)
    override fun getCommentList(postId: Long): List<CommentResponse> {
        val post = postRepository.findByIdAndDeletedAt(postId, null) ?: throw ModelNotFoundException(
            "post",
            "이 게시물 아이디: (${postId})는 존재하지 않습니다."
        )
        return commentRepository.findByPostIdAndDeletedAt(post, null).map { from(it) }
    }

    @Transactional
    override fun createComment(postId: Long, req: CommentRequest, userId: Long): String {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException(
            "user",
            "이 유저 아이디(${userId})는 존재하지 않습니다."
        )
        val post = postRepository.findByIdAndDeletedAt(postId, null) ?: throw ModelNotFoundException(
            "post",
            "이 게시물 아이디: (${postId})는 존재하지 않습니다."
        )
        val comment = commentRepository.save(
            Comment(
                content = req.content,
                userId = user,
                postId = post
            )
        )
        userLogRepository.save(
            UserLog(
                userId = user,
                postLikeId = null,
                commentLikeId = null,
                commentId = comment,
                myActiveType = MyActiveType.COMMENT_LIKES
            )
        )
        return "댓글이 작성되었습니다!"
    }

    @Transactional
    override fun deleteComment(commentId: Long, userId: Long) {
        val comment = commentRepository.findByIdAndDeletedAt(commentId, null) ?: throw ModelNotFoundException(
            "comment",
            "이 댓글(${commentId})은 존재하지 않습니다."
        )
        commentRepository.delete(comment)
    }
}