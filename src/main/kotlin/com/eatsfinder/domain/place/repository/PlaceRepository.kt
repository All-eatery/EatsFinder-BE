package com.eatsfinder.domain.place.repository

import com.eatsfinder.domain.place.model.Place
import org.springframework.data.jpa.repository.JpaRepository

interface PlaceRepository: JpaRepository<Place, Long> {
}