package com.eatsfinder.domain.place.model

import com.eatsfinder.domain.category.model.Category
import com.eatsfinder.global.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "places")
class Place(

    @Column(name = "name", length = 100, nullable = false)
    val name: String,

    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    var address: String,

    @Column(name = "road_address", columnDefinition = "TEXT")
    var roadAddress: String,

    @Column(name = "telephone", length = 15)
    var telephone: String,

    @Column(name = "x", columnDefinition = "DOUBLE", nullable = false)
    var x: Double,

    @Column(name = "y", columnDefinition = "DOUBLE", nullable = false)
    var y: Double,

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    val categoryId: Category

) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}