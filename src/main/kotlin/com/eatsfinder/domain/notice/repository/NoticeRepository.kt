package com.eatsfinder.domain.notice.repository

import com.eatsfinder.domain.notice.model.Notice
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface NoticeRepository : JpaRepository<Notice, Long> {

    fun findByIdAndDeletedAt(id: Long, deletedAt: LocalDateTime?): Notice?

    fun findAllByDeletedAt(deletedAt: LocalDateTime?): List<Notice>?
}