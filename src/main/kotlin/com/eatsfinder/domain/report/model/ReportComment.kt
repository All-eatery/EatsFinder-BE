package com.eatsfinder.domain.report.model

import com.eatsfinder.domain.comment.model.Comment
import com.eatsfinder.domain.reply.model.Reply
import com.eatsfinder.domain.user.model.User
import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault

@Entity
@Table(name = "report_comments")
class ReportComment(

    @Column(name = "reason")
    var reason: String,

    @ManyToOne
    @JoinColumn(name = "reported_user_id", nullable = false)
    val reportedUserId: User,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val userId: User,

    @ManyToOne
    @JoinColumn(name = "comment_id")
    val commentId: Comment?,

    @ManyToOne
    @JoinColumn(name = "reply_id")
    val replyId: Reply?,

    @ColumnDefault("false")
    @Column(name = "abusive")
    var abusive: Boolean = false,

    @ColumnDefault("false")
    @Column(name = "spam")
    var spam: Boolean = false,

    @ColumnDefault("false")
    @Column(name = "porno")
    var porno: Boolean = false,

    @ColumnDefault("false")
    @Column(name = "offensive")
    var offensive: Boolean = false,

    @ColumnDefault("false")
    @Column(name = "harmful")
    var harmful: Boolean = false,

    @ColumnDefault("false")
    @Column(name = "others")
    var others: Boolean = false

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}