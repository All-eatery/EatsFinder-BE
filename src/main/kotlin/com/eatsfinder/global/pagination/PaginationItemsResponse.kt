package com.eatsfinder.global.pagination

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class PaginationItemsResponse(
    @JsonProperty("totalItems") val totalItems: Long,
    @JsonProperty("itemsPerPage") val itemsPerPage: Int,
    @JsonProperty("totalPage") val totalPage: Long,
    @JsonProperty("currentPage") val currentPage: Int,
    @JsonProperty("lastPage") val isLastPage: Boolean
): Serializable
