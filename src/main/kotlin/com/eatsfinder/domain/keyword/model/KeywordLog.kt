package com.eatsfinder.domain.keyword.model

import jakarta.persistence.*

@Entity
@Table(name = "keyword_Log")
class KeywordLog(
    @Column(name = "keyword", nullable = false) var keyword: String,

    @Column(name = "count", nullable = false) var count: Long

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}