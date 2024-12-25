package com.eatsfinder.domain.place.repository

import com.eatsfinder.domain.place.model.PlaceMenus
import org.springframework.data.jpa.repository.JpaRepository

interface PlaceMenusRepository : JpaRepository<PlaceMenus, Long> {
}