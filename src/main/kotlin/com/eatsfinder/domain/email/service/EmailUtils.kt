package com.eatsfinder.domain.email.service

import jakarta.mail.MessagingException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component

@Component
class EmailUtils(
    private val javaMailSender: JavaMailSender,
) {

    fun sendEmail(email: String, authCode: String) {
        val message = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        try {
            helper.setTo(email)
            helper.setSubject("EatsFinder의 이메일 인증번호입니다!")
            helper.setText(authCode)

        } catch (e: MessagingException) {
            e.printStackTrace()
        }

        javaMailSender.send(message)
    }
}