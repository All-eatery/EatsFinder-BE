package com.eatsfinder.domain.category.model

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault

@Entity
@Table(name = "categories")
class Category(

    @Column(name = "name", length = 6, nullable = false)
    val name: String,

    @Column(name = "type", length = 6)
    val type: String,

    @Column(name = "code", length = 6)
    val code: String,

    @ColumnDefault("0")
    @Column(name = "cg_count", nullable = false)
    val cgCount: Int = 0,

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}