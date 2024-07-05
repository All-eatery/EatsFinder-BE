package com.eatsfinder.domain.category.model

import jakarta.persistence.*

@Entity
@Table(name = "categories")
class Category(

    @Column(name = "name", length = 6, nullable = false)
    val name: String,

    @Column(name = "type", length = 6)
    val type: String,

    @Column(name = "code", length = 6)
    val code: String

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}