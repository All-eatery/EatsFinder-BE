package com.eatsfinder.domain.starRating.model

import com.eatsfinder.domain.place.model.Place
import jakarta.persistence.*

@Entity
@Table(name = "star_ratings")
class StarRating(

    @Column(name = "star", nullable = false, columnDefinition = "FLOAT(2,1)")
    val star: Float,

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "post_id", nullable = true)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    val postId: Post

    @ManyToOne
    @JoinColumn(name = "place_id")
    val placeId: Place

    ) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}