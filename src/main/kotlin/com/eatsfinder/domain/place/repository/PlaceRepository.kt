package com.eatsfinder.domain.place.repository

import com.eatsfinder.domain.place.model.Place
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface PlaceRepository: JpaRepository<Place, Long> {

    fun findByDeletedAt(deletedAt: LocalDateTime?): List<Place>?
}