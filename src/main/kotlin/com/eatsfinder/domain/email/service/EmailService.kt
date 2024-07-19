package com.eatsfinder.domain.email.service

import com.eatsfinder.domain.email.dto.EmailRequest
import com.eatsfinder.domain.user.model.SocialType

interface EmailService {

    fun sendCodeToEmail(req: EmailRequest)
    fun checkVerifyCode(email: String, code: String)

    fun checkEmail(email: String, provider: SocialType): String
}