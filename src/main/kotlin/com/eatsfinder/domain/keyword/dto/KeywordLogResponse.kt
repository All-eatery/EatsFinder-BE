package com.eatsfinder.domain.keyword.dto

import com.eatsfinder.domain.keyword.model.KeywordLog
import java.io.Serializable

data class KeywordLogResponse(
    val keyword: String,
//    val count: Long
): Serializable
{
    companion object {
        fun from (keyword: KeywordLog): KeywordLogResponse{
            return KeywordLogResponse(
                keyword = keyword.keyword,
//                count = keyword.count
            )
        }
    }
}
