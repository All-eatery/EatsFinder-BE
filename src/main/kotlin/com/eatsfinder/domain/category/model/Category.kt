package com.eatsfinder.domain.category.model

import jakarta.persistence.*

@Entity
@Table(name = "categories")
class Category(

    @Column(name = "classification", length = 6)
    val classification: String

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}