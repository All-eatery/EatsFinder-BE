package com.eatsfinder.domain.user.dto.user

import com.eatsfinder.domain.post.model.Post
import org.springframework.data.domain.Pageable
import java.time.format.DateTimeFormatter


data class OtherPeopleFeedsResponse(
    val pagination: PaginationFeedResponse,
    val data: List<OtherPeopleFeedResponse>
) {
    companion object {
        fun from(posts: List<Post>, pageable: Pageable): OtherPeopleFeedsResponse {
            val createdDate = DateTimeFormatter.ofPattern("yyyy.MM.dd")
            val totalFeed = posts.size.toLong()
            val pagedFeeds = posts.drop(pageable.pageNumber * pageable.pageSize)
                .take(pageable.pageSize)

            val totalPage = if (totalFeed == 0L) {
                0L
            } else {
                (totalFeed + pageable.pageSize - 1) / pageable.pageSize
            }

            val isLastPage = (pageable.pageNumber + 1) * pageable.pageSize >= totalFeed

            val pagination = PaginationFeedResponse(
                totalFeed = totalFeed,
                feedsPerPage = pageable.pageSize,
                totalPage = totalPage,
                currentPage = pageable.pageNumber,
                isLastPage = isLastPage
            )

            val otherPersonFeedResponses = pagedFeeds.map { post ->
                OtherPeopleFeedResponse(
                    postId = post.id,
                    thumbnailUrl = post.thumbnailUrl,
                    placeName = post.placeId.name,
                    content = post.content,
                    createdAt = post.createdAt.toLocalDate().format(createdDate)
                )
            }

            return OtherPeopleFeedsResponse(pagination, otherPersonFeedResponses)
        }
    }
}
