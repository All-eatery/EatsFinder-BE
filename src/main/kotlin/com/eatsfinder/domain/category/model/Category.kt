package com.eatsfinder.domain.category.model

import jakarta.persistence.*

@Entity
@Table(name = "categories")
class Category(

    @Column(name = "name", length = 6)
    val name: String,

    @Column(name = "group", length = 6)
    val group: String,

    @Column(name = "code", length = 6)
    val code: String

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}