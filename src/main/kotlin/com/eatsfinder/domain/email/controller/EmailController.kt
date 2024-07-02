package com.eatsfinder.domain.email.controller

import com.eatsfinder.domain.email.dto.EmailRequest
import com.eatsfinder.domain.email.service.EmailService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/auth")
class EmailController(
    private val mailService: EmailService
) {

    @Operation(summary = "인증번호 보내기")
    @PostMapping("/email")
    fun sendCodeToEmail(@RequestBody req: EmailRequest): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.OK).body(mailService.sendCodeToEmail(req))
    }

    @Operation(summary = "인증번호 확인하기")
    @GetMapping("/email/verify-code")
    fun checkVerifyCode(@RequestParam email: String, @RequestParam code: String): ResponseEntity<String> {
        mailService.checkVerifyCode(email, code)
        return ResponseEntity.status(HttpStatus.OK).body(mailService.checkVerifyCode(email, code))
    }
}