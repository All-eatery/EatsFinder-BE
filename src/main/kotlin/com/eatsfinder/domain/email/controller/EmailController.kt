package com.eatsfinder.domain.email.controller

import com.eatsfinder.domain.email.dto.EmailRequest
import com.eatsfinder.domain.email.service.EmailService
import com.eatsfinder.domain.user.model.SocialType
import com.eatsfinder.global.exception.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/auth")
class EmailController(
    private val mailService: EmailService
) {

    @Operation(summary = "인증번호 보내기")
    @PostMapping("/email")
    fun sendCodeToEmail(@RequestBody @Valid req: EmailRequest): BaseResponse<Unit> {
        mailService.sendCodeToEmail(req)
        return BaseResponse(message = "인증번호가 발송되었습니다.")
    }

    @Operation(summary = "인증번호 확인하기")
    @GetMapping("/email/verify-code")
    fun checkVerifyCode(@RequestParam email: String, @RequestParam code: String): BaseResponse<Unit> {
        mailService.checkVerifyCode(email, code)
        return BaseResponse(message = "입력하신 코드는 맞는 코드입니다.")
    }

    @Operation(summary = "이메일 중복확인하기")
    @GetMapping("/email/{provider}")
    fun checkVerifyCode(@RequestParam email: String, @RequestParam provider: SocialType): BaseResponse<String> {
        val checkEmail = mailService.checkEmail(email, provider)
        return BaseResponse(message = checkEmail)
    }
}