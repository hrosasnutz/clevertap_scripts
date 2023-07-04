package com.tul.clevertap_scripts.executions

import com.tul.clevertap_scripts.ClevertapScriptsApplicationTests
import com.tul.clevertap_scripts.client.ClevertapRestClient
import mu.KotlinLogging
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class ClevertapRestClientExecution : ClevertapScriptsApplicationTests() {
    
    private val log = KotlinLogging.logger {  }
    
    @Autowired
    private lateinit var client: ClevertapRestClient

    @Test
    @Disabled
    fun deleteProfile() {
        val profiles = listOf("816")
        val result = client.deleteProfile(profiles)
        log.info { "results: $result" }
    }

    @Test
    @Disabled
    fun demergeProfile() {
        val profiles = listOf(
            "co-c2ad1276-62b1-42be-86f3-1cfc432c7917",
            "br-169a6df8-c55a-47af-8daf-7307f0418a54",
            "co-19b13a8d-e06a-4d15-94d9-51387eb06351"
        )
        val result = client.demergeProfile(profiles)
        log.info { "results: $result" }
    }
}