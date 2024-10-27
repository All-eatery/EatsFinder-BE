package com.eatsfinder.domain.user.dto.user

import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.global.pagination.PaginationItemsResponse
import org.springframework.data.domain.Pageable
import java.time.format.DateTimeFormatter

data class MyFeedsResponse(
    val pagination: PaginationItemsResponse,
    val data : List<MyFeedResponse>
) {
    companion object {
        fun from(posts: List<Post>, pageable: Pageable): MyFeedsResponse {
            val createdDate = DateTimeFormatter.ofPattern("yyyy.MM.dd")
            val totalItems = posts.size.toLong()
            val pagedItems = posts.drop(pageable.pageNumber * pageable.pageSize)
                .take(pageable.pageSize)

            val totalPage = if (totalItems == 0L) {
                0L
            } else {
                (totalItems + pageable.pageSize - 1) / pageable.pageSize
            }

            val isLastPage = (pageable.pageNumber + 1) * pageable.pageSize >= totalItems

            val pagination = PaginationItemsResponse(
                totalItems = totalItems,
                itemsPerPage = pageable.pageSize,
                totalPage = totalPage,
                currentPage = pageable.pageNumber,
                isLastPage = isLastPage
            )

            val myFeedResponses = pagedItems.map { post ->
                MyFeedResponse(
                    postId = post.id,
                    thumbnailUrl = post.thumbnailUrl,
                    placeName = post.placeId.name,
                    content = post.content,
                    createdAt = post.createdAt.toLocalDate().format(createdDate)
                )
            }

            return MyFeedsResponse(pagination, myFeedResponses)
        }
    }
}
