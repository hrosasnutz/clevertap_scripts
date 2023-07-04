package com.tul.clevertap_scripts.util

object Util {

    private val REGEX_VALID_IDENTIFY = "(co|mx|br)-([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})".toRegex()

    fun isValidIdentity(identity: String) : Boolean {
        return identity.matches(REGEX_VALID_IDENTIFY)
    }
}