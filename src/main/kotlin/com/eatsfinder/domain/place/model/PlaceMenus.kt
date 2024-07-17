package com.eatsfinder.domain.place.model

import jakarta.persistence.*

@Entity
@Table(name = "place_menus")
class PlaceMenus(

    @Column(name="menu", columnDefinition = "TEXT", nullable = false)
    val menu: String,

    @ManyToOne
    @JoinColumn(name = "place_id")
    val placeId: Place


    ) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}