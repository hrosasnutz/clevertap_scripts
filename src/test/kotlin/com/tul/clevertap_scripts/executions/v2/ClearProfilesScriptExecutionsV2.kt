package com.tul.clevertap_scripts.executions.v2

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.tul.clevertap_scripts.ClevertapScriptsApplicationTests
import com.tul.clevertap_scripts.client.ClevertapRestClient
import com.tul.clevertap_scripts.util.Util
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import java.util.concurrent.TimeUnit

class ClearProfilesScriptExecutionsV2 : ClevertapScriptsApplicationTests() {
    
    private val log = KotlinLogging.logger { }

    companion object {
        private val FILTER_ROWS = listOf(
            "Identity",
            "CleverTap Id"
        )
    }
    
    @Autowired
    private lateinit var client: ClevertapRestClient
    
    @Value("\${clevertap.csv.profiles.v2}")
    private lateinit var file: Resource
    
    @Test
    fun executeClearProfiles() {
        csvReader { autoRenameDuplicateHeaders = true }.open(file.inputStream) {
            readAllWithHeaderAsSequence()
                .mapNotNull { it.filterKeys { k -> k in FILTER_ROWS } }
                .filter { it.isNotEmpty() }
                .forEach {
                    processRow(it)
                }
        }
    }

    private fun processRow(row: Map<String, String>) {
        val identity = row["Identity"] ?: ""
        val clevertapId = row["CleverTap Id"] ?: ""
        if (identity.isEmpty()) {
            if (clevertapId.isEmpty()) {
                log.info { "preserve profile: $row" }
            } else {
                val status = client.deleteProfile(listOf(clevertapId), "guid")
                log.info { "delete profile: $row -> status: $status" }
                TimeUnit.SECONDS.sleep(1)
            }
        } else {
            if (identity.contains(",")) {
                val profiles = identity.split(",").mapNotNull { s -> s.trim() }
                val status = client.demergeProfile(profiles)
                log.info { "demerge profiles: $row -> status: $status" }
                TimeUnit.SECONDS.sleep(1)
            } else if(!Util.isValidIdentity(identity)) {
                val status = client.deleteProfile(listOf(identity))
                log.info { "delete profile: $row -> status: $status" }
                TimeUnit.SECONDS.sleep(1)
            } else {
                log.info { "preserve profile: $row" }
            }
        }
    }
}
