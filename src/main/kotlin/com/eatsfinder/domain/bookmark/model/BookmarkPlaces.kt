package com.eatsfinder.domain.bookmark.model

import com.eatsfinder.domain.place.model.Place
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
@Table(name = "bookmark_places")
class BookmarkPlaces(
    @ManyToOne
    @JoinColumn(name = "bookmark_id", nullable = false)
    val bookmarkId: Bookmark,

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    val placeId: Place

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(columnDefinition = "TIMESTAMP(6)", name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    var createdAt: LocalDateTime = LocalDateTime.now()
        protected set

    @Column(columnDefinition = "TIMESTAMP(6)", name = "updated_at", nullable = false)
    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()
        protected set

}