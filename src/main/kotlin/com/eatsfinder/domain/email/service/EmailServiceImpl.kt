package com.eatsfinder.domain.email.service

import com.eatsfinder.domain.email.dto.EmailRequest
import com.eatsfinder.domain.email.model.Email
import com.eatsfinder.domain.email.repository.EmailRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


@Service
class EmailServiceImpl(
    private val emailRepository: EmailRepository,
    private val emailUtils: EmailUtils
) : EmailService {

    @Transactional
    override fun sendCodeToEmail(req: EmailRequest) {
        val generator = EmailAuthCodeGenerator()
        val authCode = generator.executeGenerate()


        val emailAuthCode = emailRepository.findByEmail(req.email)

        if (emailAuthCode == null) {
            emailRepository.save(
                Email(
                    email = req.email,
                    code = authCode,
                    isVerification = false
                )
            )
            return
        }

        if (emailAuthCode.expiresAt.isBefore(LocalDateTime.now())) {
            emailRepository.save(
                emailAuthCode.apply {
                    code = authCode
                    isVerification = false
                    createdAt = LocalDateTime.now()
                    expiresAt = LocalDateTime.now().plusMinutes(5)
                })
            emailUtils.sendEmail(req.email, authCode)
        } else throw IllegalStateException("인증번호가 만료되지 않았습니다")

    }

    override fun checkVerifyCode(email: String, code: String): String {
        val checkCode = emailRepository.findByCode(code)
        return when {
            checkCode == null || !(checkCode.code == code && checkCode.email == email) -> "다시 한번 코드를 입력해주세요"
            checkCode.expiresAt.isBefore(LocalDateTime.now()) -> "인증 시간이 만료된 인증번호입니다"
            else -> {
                checkCode.isVerification = true
                emailRepository.save(checkCode)
                "입력하신 코드는 맞는 코드입니다"
            }
        }
    }
}

