package com.eatsfinder.domain.comment.service

import com.eatsfinder.domain.comment.dto.CommentRequest
import com.eatsfinder.domain.comment.dto.CommentResponse
import com.eatsfinder.domain.comment.dto.CommentResponse.Companion.from
import com.eatsfinder.domain.comment.model.Comment
import com.eatsfinder.domain.comment.repository.CommentRepository
import com.eatsfinder.domain.post.repository.PostRepository
import com.eatsfinder.domain.user.repository.UserRepository
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

    override fun createComment(postId: Long, req: CommentRequest): String {
        val user = userRepository.findByIdOrNull(postId) ?: TODO() // 이 postId는 수정예정
        val post = postRepository.findByIdOrNull(postId) ?: TODO()
        val comment = commentRepository.save(
            Comment(
                content = req.content,
                userId = user,
                postId = post
            )
        )
        return "댓글이 작성되었습니다!"
    }

    override fun deleteComment(commentId: Long) {
        val comment = commentRepository.findByIdOrNull(commentId) ?: TODO()
        commentRepository.delete(comment)
    }
}