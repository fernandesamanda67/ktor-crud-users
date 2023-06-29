package com.example.config.serialization

import kotlinx.serialization.json.Json

fun jsonConfig() = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
}