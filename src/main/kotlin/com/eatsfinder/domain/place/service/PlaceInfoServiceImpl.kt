package com.eatsfinder.domain.place.service

import com.eatsfinder.domain.place.dto.PlaceInfoRequest
import com.eatsfinder.domain.place.dto.PlaceInfoResponse
import com.eatsfinder.domain.place.repository.PlaceRepository
import com.eatsfinder.domain.post.model.Post
import com.eatsfinder.domain.post.repository.PostRepository
import org.springframework.stereotype.Service

@Service
class PlaceInfoServiceImpl(
    private val placeRepository: PlaceRepository,
    private val postRepository: PostRepository
): PlaceInfoService {
    override fun getPlaceMapInfo(req: PlaceInfoRequest): List<PlaceInfoResponse> {

        val places = placeRepository.findByXBetweenAndYBetween(
            req.oa,
            req.ha,
            req.qa,
            req.pa
        )

        if (places.isEmpty()) return emptyList()

        val posts = postRepository.findByPlaceIdIn(places)

        val postMap: Map<Long?, Post>? =
            posts
                ?.groupBy { it.placeId.id }
                ?.mapValues { (_, postList) ->
                    postList
                        .maxByOrNull { it.createdAt }
                }
                ?.filterValues { it != null }
                ?.mapValues { it.value!!}

        return PlaceInfoResponse.from(places, postMap)
    }
}