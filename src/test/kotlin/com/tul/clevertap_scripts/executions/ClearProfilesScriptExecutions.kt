package com.tul.clevertap_scripts.executions

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.tul.clevertap_scripts.ClevertapScriptsApplicationTests
import com.tul.clevertap_scripts.client.ClevertapRestClient
import mu.KotlinLogging
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import java.util.UUID
import java.util.concurrent.TimeUnit

class ClearProfilesScriptExecutions : ClevertapScriptsApplicationTests() {
    
    private val log = KotlinLogging.logger { }

    companion object {
        private val REGEX_VALID_IDENTIFY = "(co|mx|br)-([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})".toRegex()
    }
    
    @Autowired
    private lateinit var client: ClevertapRestClient
    
    @Value("\${clevertap.csv.profiles}")
    private lateinit var file: Resource
    
    @Test
    @Disabled
    fun testValidIdentify() {
        listOf(
            "15457",
            "co-15457",
            "${UUID.randomUUID()}",
            "er-${UUID.randomUUID()}",
            "co-${UUID.randomUUID()}"
        ).forEach { 
            log.info { "identity: $it is valid? ${isValidIdentity(it)}" }
        }
    }
    
    @Test
    fun executeClearProfiles() {
        csvReader().readAllWithHeader(file.inputStream)
            .mapNotNull { it["Identity"] }
            .filter { it.isNotEmpty() }
            .forEach {
                if (it.contains(",")) {
                    val profiles = it.split(",").mapNotNull { s -> s.trim() }
                    val status = client.demergeProfile(profiles)
                    log.info { "demerge profiles: $profiles -> status: $status" }
                    TimeUnit.SECONDS.sleep(1)
                } else if(!isValidIdentity(it)) {
                    val status = client.deleteProfile(listOf(it))
                    log.info { "delete profile: $it -> status: $status" }
                    TimeUnit.SECONDS.sleep(1)
                } else {
                    log.info { "preserve profile: $it" }
                }
            }
    }

    private fun isValidIdentity(identity: String) : Boolean {
        return identity.matches(REGEX_VALID_IDENTIFY)
    }
}
