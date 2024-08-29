package com.eatsfinder.domain.place.model

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault

@Entity
@Table(name = "place_menus")
class PlaceMenus(

    @Column(name = "menu", columnDefinition = "TEXT", nullable = false)
    val menu: String,

    @ColumnDefault("0")
    @Column(name = "pm_count", nullable = false)
    val pmCount: Int = 0,

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    val placeId: Place


    ) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}