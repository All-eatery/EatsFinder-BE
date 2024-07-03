package com.eatsfinder.global.email

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.Properties

@Configuration
class EmailConfig(
    @Value("\${mail.host}") private val host: String,
    @Value("\${mail.port}") private val port: Int,
    @Value("\${mail.username}") private val name: String,
    @Value("\${mail.password}") private val password: String,
    @Value("\${mail.properties.mail.smtp.auth}") private val auth: Boolean,
    @Value("\${mail.properties.mail.smtp.starttls.enable}") private val enable: Boolean,
    @Value("\${mail.properties.mail.smtp.starttls.required}") private val required: Boolean,
    @Value("\${mail.properties.mail.smtp.connectionTimeout}") private val connectionTimeout: Int,
    @Value("\${mail.properties.mail.smtp.timeout}") private val timeout: Int,
    @Value("\${mail.properties.mail.smtp.writeTimeout}") private val writeTimeout: Int

) {
    @Bean
    fun mailSender(): JavaMailSenderImpl {
        val mailSender = JavaMailSenderImpl()

        mailSender.host = host
        mailSender.port = port
        mailSender.username = name
        mailSender.password = password
        mailSender.defaultEncoding = "UTF-8"
        mailSender.javaMailProperties = getMailProperties()

        return mailSender

    }

    private fun getMailProperties(): Properties {
        val property = Properties()

        property["mail.smtp.auth"] = auth
        property["mail.smtp.starttls.enable"] = enable
        property["mail.smtp.starttls.required"] = required
        property["mail.smtp.connectionTimeout"] = connectionTimeout
        property["mail.smtp.timeout"] = timeout
        property["mail.smtp.writeTimeout"] = writeTimeout

        return property
    }
}