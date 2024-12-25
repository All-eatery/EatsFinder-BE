package com.eatsfinder.domain.bookmark.repository

import com.eatsfinder.domain.bookmark.model.Bookmark
import org.springframework.data.jpa.repository.JpaRepository

interface BookmarkRepository : JpaRepository<Bookmark, Long> {
}