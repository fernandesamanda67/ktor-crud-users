package com.example.config.routes

import com.example.routes.userRouting
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        userRouting()
    }
}
