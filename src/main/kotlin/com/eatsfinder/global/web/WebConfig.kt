package com.eatsfinder.global.web

import com.eatsfinder.global.oauth.converter.OAuth2ProviderConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    @Value("\${kApi.url}") private val kApiUrl: String
) : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(OAuth2ProviderConverter())
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(
                "http://localhost:8080", "http://localhost:8090", "http://localhost:3000", kApiUrl,
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3000)
    }
}