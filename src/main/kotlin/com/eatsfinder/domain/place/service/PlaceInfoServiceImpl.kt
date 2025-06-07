package com.eatsfinder.domain.place.service

import com.eatsfinder.domain.place.dto.PaginationPlaceInfoResponse
import com.eatsfinder.domain.place.dto.PlaceInfoRequest
import com.eatsfinder.domain.place.dto.PlaceInfoResponse
import com.eatsfinder.domain.place.repository.PlaceRepository
import com.eatsfinder.global.pagination.PaginationItemsResponse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PlaceInfoServiceImpl(
    private val placeRepository: PlaceRepository
): PlaceInfoService {
    override fun getPlaceMapInfo(req: PlaceInfoRequest,cursorId: Long?, pageSize: Int): PaginationPlaceInfoResponse {
        val pageable: Pageable = PageRequest.of(0, pageSize + 1)
        val maps = when (cursorId) {
            null -> placeRepository.findByXBetweenAndYBetween(req.oa, req.ha, req.qa, req.pa, pageable)
            else -> placeRepository.findByXBetweenAndYBetweenAndIdGreaterThan(req.oa, req.ha, req.qa, req.pa, cursorId, pageable)
        }

        val mapList: List<PlaceInfoResponse> = maps.map { map ->
            PlaceInfoResponse(
                id = map.id,
                name = map.name,
                x = map.x,
                y = map.y
            )
        }

        val mapCursor = when (cursorId) {
            null -> mapList.take(pageSize)
            else -> mapList.filter { it.id!! > cursorId }.take(pageSize)
        }

        val isLastPage = mapList.size <= pageSize

        val nextCursorId = when {
            mapCursor.isEmpty() -> null
            mapCursor.size > pageSize -> mapList[pageSize - 1].id
            else -> mapCursor.last().id
        }

        val totalCount = placeRepository.countXBetweenAndYBetween(req.oa, req.ha, req.qa, req.pa)
        val pagination = PaginationItemsResponse(
            totalItems = totalCount,
            itemsPerPage = pageSize,
            totalPage = (mapList.size / pageSize).toLong() + if (mapList.size % pageSize > 0) 1 else 0,
            currentPage = 1,
            isLastPage = isLastPage
        )


        return PaginationPlaceInfoResponse(
            pagination = pagination,
            items = mapList.take(pageSize),
            lastItemId = nextCursorId
        )
    }
}