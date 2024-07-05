package com.eatsfinder.domain.email.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class EmailRequest(
    @field: NotBlank
    @field: Pattern(
        regexp = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$",
        message = "이메일의 형식에 맞게 입력해주세요"
    )
    @JsonProperty("email")
    private val _email: String?
) {
    val email: String
        get() = _email!!
}