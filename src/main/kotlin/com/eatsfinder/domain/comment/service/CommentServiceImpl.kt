package com.eatsfinder.domain.comment.service

import com.eatsfinder.domain.comment.dto.CommentRequest
import com.eatsfinder.domain.comment.dto.CommentResponse
import com.eatsfinder.domain.comment.dto.CommentResponse.Companion.from
import com.eatsfinder.domain.comment.model.Comment
import com.eatsfinder.domain.comment.repository.CommentRepository
import com.eatsfinder.domain.post.repository.PostRepository
import com.eatsfinder.domain.user.repository.UserRepository
import com.eatsfinder.global.exception.ModelNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository
) : CommentService {
    override fun getCommentList(postId: Long): List<CommentResponse> {
        return commentRepository.findAll().map { from(it) }
    }

    override fun createComment(postId: Long, req: CommentRequest, userId: Long): String {
        val user = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("user", "이 유저 아이디(${userId})는 존재하지 않습니다.")
        val post = postRepository.findByIdOrNull(postId) ?: throw ModelNotFoundException("post", "이 게시물 아이디: (${postId})는 존재하지 않습니다.")
        commentRepository.save(
            Comment(
                content = req.content,
                userId = user,
                postId = post
            )
        )
        return "댓글이 작성되었습니다!"
    }

    override fun deleteComment(commentId: Long, userId: Long) {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException("comment", "이 댓글(${commentId})은 존재하지 않습니다.")
        commentRepository.delete(comment)
    }
}