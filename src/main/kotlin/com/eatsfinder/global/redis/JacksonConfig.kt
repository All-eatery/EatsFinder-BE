package com.eatsfinder.global.redis

    import com.fasterxml.jackson.databind.ObjectMapper
    import com.fasterxml.jackson.databind.SerializationFeature
    import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
    import org.springframework.context.annotation.Bean
    import org.springframework.context.annotation.Configuration
    import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
    import java.text.SimpleDateFormat

@Configuration
    class JacksonConfig {

        @Bean
        fun objectMapper(): ObjectMapper {
            val objectMapper = Jackson2ObjectMapperBuilder.json().build<ObjectMapper>()
            objectMapper.registerModule(JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setDateFormat(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
            return objectMapper
        }
}