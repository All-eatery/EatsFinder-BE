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

    override fun createComment(postId: Long, req: CommentRequest, userId: Long): String {
        val user = userRepository.findByIdOrNull(userId) ?: TODO()
        val post = postRepository.findByIdOrNull(postId) ?: TODO()
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
        // 예외처리는 추후 추가 할 예정, 삭제 로직 확실해 지면 할 예정
        val comment = commentRepository.findByIdOrNull(commentId) ?: TODO()
        commentRepository.delete(comment)
    }
}