package com.eatsfinder.domain.email.service

import jakarta.mail.MessagingException
import jakarta.mail.internet.MimeMessage
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
            message.setRecipients(MimeMessage.RecipientType.TO, email)
            helper.setSubject("[EatsFinder] 요청하신 이메일 인증번호 안내 메일입니다")
            var body = ""
            body += (" <div" 																																																	+
                    "	style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 460px; height: 600px; border-top: 4px solid #f89b00; margin: 100px auto; padding: 30px 0; box-sizing: border-box; color: black;\"> "		+
                    "	<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400; color: black;\">"																															+
                    "		<span style=\"font-size: 15px; margin: 0 0 10px 3px; color: black;\">EatsFinder</span><br />"																													+
                    "		<span style=\"color: #f89b00\">메일인증</span> 안내입니다.<br />"																																				+
                    "	</h1>\n"																																																+
                    "	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px; color: black;\">"																							+
                    "		안녕하세요.<br />"																																													+
                    "		EatsFinder입니다!<br />"																																						+
                    "		아래 <b style=\"color: #f89b00\">'인증 번호'</b> 을 입력하여 마저 작성해 주세요.<br />"																													+
                    "		참고로, 본 보안 코드는 <b style=\"color: #f89b00\">5분 후</b>에 만료됩니다!</br>\t\t감사합니다</br>\t"																																															+
                    "	</p>"																																																	+
                    "		<p"																																																	+
                    "			style=\"display: inline-block; width: 210px; height: 45px; margin: 30px 5px 40px; background: #f89b00; line-height: 45px; vertical-align: middle; font-size: 16px; color: #FFF; text-decoration: none; text-align: center;\">"							+
                    "			${authCode}</p>"																																														+
                    "	</a>"																																																	+
                    "	<div style=\"border-top: 1px solid #DDD; padding: 5px;\"></div>"																																		+
                    " </div>"
                    )
            message.setText(body, "UTF-8", "html")

        } catch (e: MessagingException) {
            e.printStackTrace()
        }

        javaMailSender.send(message)
    }
}