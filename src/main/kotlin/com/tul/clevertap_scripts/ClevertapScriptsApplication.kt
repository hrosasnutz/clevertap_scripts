package com.tul.clevertap_scripts

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry

@EnableRetry
@SpringBootApplication
class ClevertapScriptsApplication

fun main(args: Array<String>) {
	runApplication<ClevertapScriptsApplication>(*args)
}
