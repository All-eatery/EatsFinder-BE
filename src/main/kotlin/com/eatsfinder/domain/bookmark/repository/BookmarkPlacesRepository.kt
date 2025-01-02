package com.eatsfinder.domain.bookmark.repository

import com.eatsfinder.domain.bookmark.model.BookmarkPlaces
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface BookmarkPlacesRepository : JpaRepository<BookmarkPlaces, Long> {

    @Query("SELECT bp FROM BookmarkPlaces bp WHERE bp.bookmarkId.userId.id = :userId")
    fun findByBookmarkIdUserId(@Param("userId") userId: Long): List<BookmarkPlaces>
}