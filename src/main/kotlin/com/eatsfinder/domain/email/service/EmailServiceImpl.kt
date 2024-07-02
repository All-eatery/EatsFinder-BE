package com.eatsfinder.domain.email.service

import com.eatsfinder.domain.email.dto.EmailRequest
import com.eatsfinder.domain.email.model.Email
import com.eatsfinder.domain.email.repository.EmailRepository
import com.eatsfinder.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


@Service
class EmailServiceImpl(
    private val userRepository: UserRepository,
    private val emailRepository: EmailRepository,
    private val emailUtils: EmailUtils
) : EmailService {
    @Transactional
    override fun sendCodeToEmail(req: EmailRequest) {
        this.checkDuplicatedEmail(req.email)
        val generator = EmailAuthCodeGenerator()
        val authCode = generator.executeGenerate()


        val emailAuthCode = emailRepository.findByEmail(req.email).orElseGet {
            Email(
                email = req.email,
                code = "",
                isVerification = true
            )

        }
        if (emailAuthCode.expiresAt.isBefore(LocalDateTime.now())) {
            emailAuthCode.code = authCode
            emailAuthCode.createdAt = LocalDateTime.now()
            emailAuthCode.expiresAt = LocalDateTime.now().plusMinutes(5)
            emailRepository.save(emailAuthCode)
            emailUtils.sendEmail(req.email, authCode)
        } else throw TODO()
            // 필요한 Exception("인증번호가 만료되지 않았습니다!")

    }

    override fun checkVerifyCode(email: String, code: String): String {
        TODO("Not yet implemented")
    }

    private fun checkDuplicatedEmail(email: String) {
        val user = userRepository.findByEmail(email)
        if (user.equals(email)) {
            throw TODO()
        }
    }

}

