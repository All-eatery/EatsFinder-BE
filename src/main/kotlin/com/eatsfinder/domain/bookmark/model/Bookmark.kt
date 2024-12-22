package com.eatsfinder.domain.bookmark.model

import com.eatsfinder.domain.user.model.User
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
@Table(name = "bookmarks")
class Bookmark(

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "count", length = 10)
    var count: Int,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val userId: User,

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