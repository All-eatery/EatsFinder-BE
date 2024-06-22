package com.eatsfinder.domain.comment.service

import com.eatsfinder.domain.comment.dto.CommentRequest
import com.eatsfinder.domain.comment.dto.CommentResponse
import com.eatsfinder.domain.comment.repository.CommentRepository
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl(
    private val commentRepository: CommentRepository
) : CommentService {
    override fun getCommentList(postId: Long): List<CommentResponse> {
        TODO("Not yet implemented")
    }

    override fun createComment(req: CommentRequest): String {
        TODO("Not yet implemented")
    }

    override fun deleteComment(commentId: Long) {
        TODO("Not yet implemented")
    }
}