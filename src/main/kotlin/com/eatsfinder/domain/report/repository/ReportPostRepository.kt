package com.eatsfinder.domain.report.repository

import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.domain.report.model.ReportPost
import com.eatsfinder.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface ReportPostRepository : JpaRepository<ReportPost, Long> {

    fun existsByIdAndReportedUserId(postId: Long?, reportedUserId: User?): Boolean

    fun findByPostIdAndReportedUserId(postId: Post, reportedUserId: User): ReportPost?

    fun existsByPostIdAndReportedUserIdAndUserId(postId: Post, reportedUserId: User, userId: User?): Boolean

}