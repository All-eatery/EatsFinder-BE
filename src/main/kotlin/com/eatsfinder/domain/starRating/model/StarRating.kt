package com.eatsfinder.domain.starRating.model

import com.eatsfinder.domain.place.model.Place
import jakarta.persistence.*

@Entity
@Table(name = "star_ratings")
class StarRating(

    @Column(name = "star", nullable = false, columnDefinition = "TINYINT")
    val star: Int,

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    val placeId: Place

    ) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}