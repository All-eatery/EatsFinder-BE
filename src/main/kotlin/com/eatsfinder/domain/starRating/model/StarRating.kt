package com.eatsfinder.domain.starRating.model

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

) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}