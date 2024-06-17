package com.eatsfinder

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EatsFinderApplication

fun main(args: Array<String>) {
	runApplication<EatsFinderApplication>(*args)
}
