package com.eatsfinder.domain.like.service

import com.eatsfinder.domain.comment.repository.CommentRepository
import com.eatsfinder.domain.like.dto.CommentLikeResponse
import com.eatsfinder.domain.like.model.CommentLikes
import com.eatsfinder.domain.like.repository.CommentLikeRepository
import com.eatsfinder.domain.user.repository.UserRepository
import com.eatsfinder.global.exception.ModelNotFoundException
import com.eatsfinder.global.exception.profile.MyProfileException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CommentLikeServiceImpl(
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository,
    private val commentLikeRepository: CommentLikeRepository
): CommentLikeService {
    override fun createCommentLikes(userId: Long, commentId: Long) {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException("user", "이 유저 아이디(${userId})는 존재하지 않습니다.")
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException("comment", "이 댓글 아이디: (${commentId})는 존재하지 않습니다.")
        val commentLike = commentLikeRepository.findByUserIdAndCommentId(user, comment)

        if (comment.userId == commentLike?.userId){
            throw MyProfileException("본인 댓글이므로 좋아요를 할 수 없습니다.")
        }

        if (commentLike == null){
            commentLikeRepository.save(
                CommentLikes(
                    userId = user,
                    commentId = comment
                )
            )
            comment.likeCount++
            commentRepository.save(comment)
        } else {
            throw ModelNotFoundException("like", "좋아요(${commentId})는 한번 밖에 하지 못합니다.")
        }
    }

    override fun deleteCommentLikes(userId: Long, commentId: Long) {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException("user", "이 유저 아이디(${userId})는 존재하지 않습니다.")
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException("post", "이 댓글 아이디: (${commentId})는 존재하지 않습니다.")
        val commentLike = commentLikeRepository.findByUserIdAndCommentId(user, comment)

        if(comment.userId == commentLike?.userId){
            throw MyProfileException("본인 댓글이므로 좋아요를 취소 할 수 없습니다.")
        }

        if (commentLike != null){
            commentLikeRepository.delete(commentLike)
            comment.likeCount--
            commentRepository.save(comment)
        } else {
            throw ModelNotFoundException("like", "좋아요(${commentId})는 존재하지 않아 취소할 수 없습니다.")
        }
    }

    override fun getCommentLikes(userId: Long): List<CommentLikeResponse> {
        val user = userRepository.findByIdAndDeletedAt(userId, null) ?: throw ModelNotFoundException("user", "이 유저 아이디(${userId})는 존재하지 않습니다.")
        return commentLikeRepository.findByUserId(user).map { CommentLikeResponse.from(it) }
    }
}