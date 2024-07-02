package com.eatsfinder.domain.email.service

import com.eatsfinder.domain.email.dto.EmailRequest

interface EmailService {

    fun sendCodeToEmail(req: EmailRequest)
    fun checkVerifyCode(code: String): String
}