package com.eatsfinder.domain.starRating.repository

import com.eatsfinder.domain.place.model.Place
import com.eatsfinder.domain.starRating.model.StarRating
import org.springframework.data.jpa.repository.JpaRepository

interface StarRatingRepository : JpaRepository<StarRating, Long> {

    fun findByPlaceId(placeId: Place): StarRating?
}