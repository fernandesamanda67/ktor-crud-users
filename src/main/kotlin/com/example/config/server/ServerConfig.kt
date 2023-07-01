package com.example.config.server

import com.example.config.database.initDB
import com.example.config.dependencyinjection.repositoryModules
import com.example.config.dependencyinjection.serviceModules
import com.example.config.dependencyinjection.utilModules
import com.example.config.exception.handler
import com.example.config.routes.configureRouting
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.ktor.plugin.Koin

@ExperimentalSerializationApi
fun ktorServer() = embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
    installContentNegotiator()
    configureRouting()
    initDB()
    handler()
    installDependencyInjection()
}

fun Application.installContentNegotiator() {
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            }
        )
    }
}

@ExperimentalSerializationApi
fun Application.installDependencyInjection() = install(Koin) {
    modules(
        repositoryModules(),
        serviceModules(),
        utilModules()
    )
}
