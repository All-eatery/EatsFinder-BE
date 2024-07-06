package com.eatsfinder.domain.post.model

import jakarta.persistence.*

@Entity
@Table(name = "post_keywords")
class PostKeyword(

    @Column(name = "category", length = 10, nullable = false)
    val category: String,

    @Column(name = "reaction", length = 15, nullable = false)
    val reaction: String

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}