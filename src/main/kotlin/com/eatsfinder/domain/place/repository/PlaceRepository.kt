package com.eatsfinder.domain.place.repository

import com.eatsfinder.domain.place.model.Place
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PlaceRepository : JpaRepository<Place, Long> {

    @Query("SELECT p FROM Place p  WHERE p.y BETWEEN :qa AND :pa AND p.x BETWEEN :oa AND :ha")
    fun findByXBetweenAndYBetween(
        @Param("oa") oa: Double,
        @Param("ha") ha: Double,
        @Param("qa") qa: Double,
        @Param("pa") pa: Double,
    ): List<Place>

    @Query(
        """
        SELECT p FROM Place p 
        WHERE 
        p.address LIKE %:keyword% OR
        p.categoryId.name LIKE %:keyword%
    """
    )
    fun findAllByKeywords(@Param("keyword") keyword: String, pageable: Pageable): List<Place>

    @Query(
        """
    SELECT p FROM Place p 
    WHERE 
    (p.address LIKE %:keyword% OR
    p.categoryId.name LIKE %:keyword%)
    AND p.id > :placeCursorId
"""
    )
    fun findAllByKeywordsAndIdGreaterThan(
        @Param("keyword") keyword: String,
        @Param("placeCursorId") placeCursorId: Long?,
        pageable: Pageable
    ): List<Place>

    @Query(
        """
        SELECT COUNT(p) FROM Place p
        WHERE
        p.address LIKE %:keyword% OR
        p.categoryId.name LIKE %:keyword%
    """
    )
    fun countTotalByKeyword(@Param("keyword") keyword: String): Long


}