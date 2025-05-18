package com.eatsfinder.global.redis

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration


@EnableCaching
@Configuration
class RedisConfig(
    @Value("\${spring.data.redis.host}") val host: String,
    @Value("\${spring.data.redis.port}") val port: Int,
) {
    @Bean
    fun lettuceConnectionFactory(): LettuceConnectionFactory {
        val config = RedisStandaloneConfiguration().apply {
            hostName = host
            this.port = port
        }
        return LettuceConnectionFactory(config)
    }

    @Bean
    fun redisCacheManager(redisConnectionFactory: RedisConnectionFactory, objectMapper: ObjectMapper): RedisCacheManager {
        val genericJackson2JsonRedisSerializer = GenericJackson2JsonRedisSerializer(objectMapper)

        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(
                RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofMinutes(30))
                    .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(genericJackson2JsonRedisSerializer)
                    )
            )
            .build()
    }


    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory, objectMapper: ObjectMapper): RedisTemplate<*, *> {
        val genericJackson2JsonRedisSerializer = GenericJackson2JsonRedisSerializer(objectMapper)

        return RedisTemplate<Any, Any>().apply {
            this.connectionFactory = lettuceConnectionFactory()
            this.keySerializer = StringRedisSerializer()
            this.valueSerializer = StringRedisSerializer()
            this.hashKeySerializer = StringRedisSerializer()
            this.hashValueSerializer = genericJackson2JsonRedisSerializer
        }
    }
}
