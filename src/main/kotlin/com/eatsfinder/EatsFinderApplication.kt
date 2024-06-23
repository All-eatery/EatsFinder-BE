package com.eatsfinder

import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories
@EnableJpaAuditing
@SpringBootApplication
class EatsFinderApplication

fun main(args: Array<String>){
    val dotenv = dotenv {
        directory = "./"
        ignoreIfMalformed = true
        ignoreIfMissing = true
    }

    dotenv.entries().forEach { entry ->
        System.setProperty(entry.key, entry.value)
    }

    runApplication<EatsFinderApplication>(*args)
}