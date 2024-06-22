package com.eatsfinder

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories
@EnableJpaAuditing
@SpringBootApplication
class EatsFinderApplication

fun main(args: Array<String>) {
	runApplication<EatsFinderApplication>(*args)
}
