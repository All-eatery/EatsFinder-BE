package com.eatsfinder.domain.email.service

import com.eatsfinder.domain.email.dto.EmailRequest
import com.eatsfinder.domain.email.model.Email
import com.eatsfinder.domain.email.repository.EmailRepository
import com.eatsfinder.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


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


        val emailAuthCode = emailRepository.findByEmail(req.email).run {
            emailRepository.save(
                Email(
                    email = req.email,
                    code = "",
                    isVerification = true
                )
            )
        }
        emailAuthCode.code = authCode
        emailUtils.sendEmail(req.email, authCode)

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

