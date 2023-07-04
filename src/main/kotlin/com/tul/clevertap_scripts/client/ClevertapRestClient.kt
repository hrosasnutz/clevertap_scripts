package com.tul.clevertap_scripts.client

import com.tul.clevertap_scripts.util.ApiTypes
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.RequestEntity
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate

@Component
class ClevertapRestClient(
    @Value("\${clevertap.api.url}")
    private val url: String,
    @Value("\${clevertap.api.user}")
    private val user: String,
    @Value("\${clevertap.api.password}")
    private val password: String
) {
    
    private lateinit var clevertapRestTemplate: RestTemplate
    
    @PostConstruct
    fun init() {
        clevertapRestTemplate = RestTemplateBuilder()
            .defaultHeader("X-CleverTap-Account-Id", user)
            .defaultHeader("X-CleverTap-Passcode", password)
            .build()
    }
    
    @Retryable(
        value = [HttpServerErrorException::class, HttpClientErrorException::class],
        maxAttempts = 1000,
        backoff = Backoff(delay = 5000)
    )
    fun deleteProfile(
        profiles: Collection<String>,
        tag: String = "identity"
    ): Map<String, Any?>? {
        return clevertapRestTemplate.exchange(
            RequestEntity.post("$url/1/delete/profiles.json")
                .body(mapOf(tag to profiles)),
            ApiTypes.map()
        ).body
    }

    @Retryable(
        value = [HttpServerErrorException::class, HttpClientErrorException::class],
        maxAttempts = 1000,
        backoff = Backoff(delay = 5000)
    )
    fun demergeProfile(
        profiles: Collection<String>,
        tag: String = "identities"
    ): Map<String, Any?>? {
        return clevertapRestTemplate.exchange(
            RequestEntity.post("$url/1/demerge/profiles.json")
                .body(mapOf(tag to profiles)),
            ApiTypes.map()
        ).body
    }
}
