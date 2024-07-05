package com.eatsfinder.domain.post.model

import jakarta.persistence.*

@Entity
@Table(name = "post_keywords")
class PostKeyword(

    @Column(name = "category", length = 10)
    val category: String,

    @Column(name = "reaction", length = 15)
    val reaction: String

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}