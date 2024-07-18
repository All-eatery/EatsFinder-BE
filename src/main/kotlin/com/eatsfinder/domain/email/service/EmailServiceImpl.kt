package com.eatsfinder.domain.email.service

import com.eatsfinder.domain.email.dto.EmailRequest
import com.eatsfinder.domain.email.model.Email
import com.eatsfinder.domain.email.repository.EmailRepository
import com.eatsfinder.domain.user.repository.UserRepository
import com.eatsfinder.global.exception.ModelNotFoundException
import com.eatsfinder.global.exception.email.ExpiredCodeException
import com.eatsfinder.global.exception.email.NotGenerateCodeException
import com.eatsfinder.global.exception.email.OneTimeMoreWriteException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


@Service
class EmailServiceImpl(
    private val emailRepository: EmailRepository,
    private val userRepository: UserRepository,
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
                    code = authCode
                )
            )
            emailUtils.sendEmail(req.email, authCode)
            return
        }

        if (emailAuthCode.expiredAt.isBefore(LocalDateTime.now())) {
            emailRepository.save(
                emailAuthCode.apply {
                    code = authCode
                    createdAt = LocalDateTime.now()
                    expiredAt = LocalDateTime.now().plusMinutes(5)
                })
            emailUtils.sendEmail(req.email, authCode)
            return
        } else throw NotGenerateCodeException("인증번호가 만료되지 않았습니다")

    }

    @Transactional(readOnly = true)
    override fun checkVerifyCode(email: String, code: String) {
        val checkCode = emailRepository.findByCode(code)
        when {
            checkCode == null || !(checkCode.code == code && checkCode.email == email) -> throw OneTimeMoreWriteException("다시 한번 코드를 입력해주세요")
            checkCode.expiredAt.isBefore(LocalDateTime.now()) -> throw ExpiredCodeException("인증 시간이 만료된 인증번호입니다")
            else -> {
                checkCode.complete = true
                emailRepository.save(checkCode)
            }
        }
    }

    @Transactional(readOnly = true)
    override fun checkEmail(email: String): String {
        val existUser = userRepository.existsByEmailAndDeletedAt(email, null)
        if (existUser) {
            throw ModelNotFoundException("email", "이미 가입되어 있는 이메일입니다 : $email")
        }
        return "가입 가능한 이메일입니다."
    }
}

