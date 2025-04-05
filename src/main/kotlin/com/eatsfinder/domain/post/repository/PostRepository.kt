package com.eatsfinder.domain.post.repository

import com.eatsfinder.domain.place.model.Place
import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.domain.user.model.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface PostRepository : JpaRepository<Post, Long>, IPostRepository {

    fun findByUserId(userId: User): List<Post>?
    fun findByPlaceId(placeId: Place): List<Post>?
    fun findByIdAndDeletedAt(id: Long, deletedAt: LocalDateTime?): Post?

    @Query("SELECT p FROM Post p WHERE p.userId IN :userIds AND p.updatedAt > :updatedAt AND p.deletedAt IS NULL ORDER BY p.updatedAt ASC")
    fun findPostByUserIdInAndOrderByUpdatedAtAfter(userIds: List<User>, updatedAt: LocalDateTime): List<Post>?

    @Query(
        """
        SELECT p FROM Post p 
        WHERE 
        (p.placeId.name LIKE %:keyword% OR
        p.placeId.address LIKE %:keyword% OR
        p.userId.nickname LIKE %:keyword% OR
        p.content LIKE %:keyword% OR
        p.placeId.categoryId.name LIKE %:keyword% OR
        EXISTS (
            SELECT 1 FROM PlaceMenus pm 
            WHERE pm.placeId = p.placeId AND pm.menu LIKE %:keyword%
        ))
    """
    )
    fun findAllByKeywords(@Param("keyword") keyword: String, pageable: Pageable): List<Post>

    @Query(
        """
        SELECT p FROM Post p 
        WHERE p.id > :postCursorId AND (
        p.placeId.name LIKE %:keyword% OR
        p.placeId.address LIKE %:keyword% OR
        p.userId.nickname LIKE %:keyword% OR
        p.content LIKE %:keyword% OR
        p.placeId.categoryId.name LIKE %:keyword% OR
        EXISTS (
            SELECT 1 FROM PlaceMenus pm 
            WHERE pm.placeId = p.placeId AND pm.menu LIKE %:keyword%
        ))
    """
    )
    fun findAllByKeywordsAndIdGreaterThan(
        @Param("keyword") keyword: String,
        @Param("postCursorId") postCursorId: Long?,
        pageable: Pageable
    ): List<Post>

    @Query(
        """
    SELECT COUNT(p) FROM Post p 
    WHERE (
    p.placeId.name LIKE %:keyword% OR
    p.placeId.address LIKE %:keyword% OR
    p.userId.nickname LIKE %:keyword% OR
    p.content LIKE %:keyword% OR
    p.placeId.categoryId.name LIKE %:keyword% OR
    EXISTS (
        SELECT 1 FROM PlaceMenus pm 
        WHERE pm.placeId = p.placeId AND pm.menu LIKE %:keyword%
    ))
"""
    )
    fun countTotalByKeyword(@Param("keyword") keyword: String): Long
}
