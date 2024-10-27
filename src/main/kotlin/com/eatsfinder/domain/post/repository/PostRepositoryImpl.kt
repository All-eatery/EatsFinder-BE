package com.eatsfinder.domain.post.repository

import com.eatsfinder.domain.like.model.QPostLikes
import com.eatsfinder.domain.post.dto.TopPostResponse
import com.eatsfinder.domain.post.model.QPost
import com.eatsfinder.domain.report.model.QReportPost
import com.eatsfinder.global.queryDsl.QueryDslSupport
import com.querydsl.core.types.Projections
import org.springframework.stereotype.Repository


@Repository
class PostRepositoryImpl: IPostRepository, QueryDslSupport() {
    private val post = QPost.post
    private val postLike = QPostLikes.postLikes
    private val reportPost = QReportPost.reportPost
    override fun getTopPostList(userId: Long?): List<TopPostResponse> {
        return if (userId == null) {
            nonLoginStatus()
        } else {
            loginStatus(userId)
        }
    }

    private fun nonLoginStatus(): List<TopPostResponse> {
        return queryFactory.select(
            Projections.constructor(
                TopPostResponse::class.java,
                post.id,
                post.placeId.name,
                post.thumbnailUrl,
                postLike.postId.id.isNotNull,
                post.likeCount,
                post.userId.profileImage,
                post.userId.nickname
            )
        )
            .from(post)
            .leftJoin(postLike).on(postLike.postId.eq(post))
            .orderBy(post.likeCount.desc())
            .limit(20)
            .fetch()
    }

    private fun loginStatus(userId: Long): List<TopPostResponse> {
        val reportedPost = queryFactory
            .select(reportPost.postId.id)
            .from(reportPost)
            .where(reportPost.userId.id.eq(userId))
            .fetch()


        return queryFactory.select(
            Projections.constructor(
                TopPostResponse::class.java,
                post.id,
                post.placeId.name,
                post.thumbnailUrl,
                postLike.postId.id.isNotNull,
                post.likeCount,
                post.userId.profileImage,
                post.userId.nickname
            )
        )
            .from(post)
            .leftJoin(postLike)
            .on(postLike.postId.eq(post))
            .where(post.id.notIn(reportedPost))
            .orderBy(post.likeCount.desc())
            .limit(20)
            .fetch()
    }
}