package com.eatsfinder.domain.report.model

import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.domain.user.model.User
import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault

@Entity
@Table(name = "report_posts")
class ReportPost(

    @Column(name = "reason")
    var reason: String,

    @ManyToOne
    @JoinColumn(name = "reported_user_id", nullable = false)
    val reportedUserId: User,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val userId: User,

    @ManyToOne
    @JoinColumn(name = "post_id")
    val postId: Post,

    @ColumnDefault("false")
    @Column(name = "different_place")
    var differentPlace: Boolean = false,

    @ColumnDefault("false")
    @Column(name = "different_menu")
    var differentMenu: Boolean = false,

    @ColumnDefault("false")
    @Column(name = "same_review")
    var sameReview: Boolean = false,

    @ColumnDefault("false")
    @Column(name = "different_price")
    var differentPrice: Boolean = false,

    @ColumnDefault("false")
    @Column(name = "closed")
    var closed: Boolean = false,

    @ColumnDefault("false")
    @Column(name = "others")
    var others: Boolean = false

    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null
}