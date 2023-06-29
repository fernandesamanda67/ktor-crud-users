package com.example

import com.example.config.server.ktorServer
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
fun main() {
    ktorServer().start(wait = true)
}
