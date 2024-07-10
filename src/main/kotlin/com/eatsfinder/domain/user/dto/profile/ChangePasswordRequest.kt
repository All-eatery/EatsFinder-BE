package com.eatsfinder.domain.user.dto.profile

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class ChangePasswordRequest(
    @field: NotBlank
    @field: Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*])[a-zA-Z0-9!@#\$%^&*]{8,15}\$",
        message = "영문, 숫자, 특수문자를 포함한 8~16자리로 입력해주세요"
    )
    @JsonProperty("newPassword")
    private val _newPassword: String,

    @field: NotBlank
    val passwordConfirm: String
){
    val newPassword: String
        get() = _newPassword
}
//    @field: Pattern(
//        regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*])[a-zA-Z0-9!@#\$%^&*]{8,15}\$",
//        message = "영문, 숫자, 특수문자를 포함한 8~16자리로 입력해주세요"
//    )
//
//    private val _changePassword: String?,
//
//    val passwordConfirm: String?
//) {
//
//    val changePassword: String?
//        get() = _changePassword
//}