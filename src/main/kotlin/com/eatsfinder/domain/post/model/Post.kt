package com.eatsfinder.domain.post.model

import com.eatsfinder.domain.category.model.Category
import com.eatsfinder.domain.comment.model.Comment
import com.eatsfinder.domain.place.model.Place
import com.eatsfinder.domain.starRating.model.StarRating
import com.eatsfinder.domain.user.model.User
import com.eatsfinder.global.entity.BaseTimeEntity
import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault

@Entity
@Table(name = "posts")
class Post(

    @Column(name = "thumbnail_url", nullable = false, columnDefinition = "TEXT")
    val thumbnailUrl: String,

    @Column(name = "image_url", nullable = true, columnDefinition = "TEXT")
    val imageUrl: String,

    @Column(name = "content", columnDefinition = "TEXT")
    val content: String,

    @Column(name = "menu_tag", columnDefinition = "TEXT")
    val menuTag: String,

    @ColumnDefault("0")
    @Column(name = "like_count", nullable = false)
    val likeCount: Int = 0,

    @Column(name = "is_owner", nullable = false, columnDefinition = "TINYINT(1)")
    val isOwner: Boolean,

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    val userId: User,

    @ManyToOne
    @JoinColumn(name = "places_id")
    val placeId: Place,

    @ManyToOne
    @JoinColumn(name = "ratings_id", nullable = false)
    val ratingId: StarRating,

    @ManyToOne
    @JoinColumn(name = "keywords_id", nullable = false)
    val keywordId: PostKeyword,

    @OneToMany(mappedBy = "postId", cascade = [CascadeType.ALL], orphanRemoval = true)
    var comments: MutableList<Comment> = mutableListOf()

) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}