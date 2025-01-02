package com.eatsfinder.domain.place.repository

import com.eatsfinder.domain.place.model.Place
import com.eatsfinder.domain.place.model.PlaceMenus
import org.springframework.data.jpa.repository.JpaRepository

interface PlaceMenusRepository : JpaRepository<PlaceMenus, Long> {
    fun findByPlaceIdAndMenu(placeId: Place, menu: String): PlaceMenus?
}