package com.tul.clevertap_scripts.util

import org.springframework.core.ParameterizedTypeReference

object ApiTypes {

    inline fun <reified T> parameterized() = object : ParameterizedTypeReference<T>() {}
    
    inline fun map() = parameterized<Map<String, Any?>>()
}