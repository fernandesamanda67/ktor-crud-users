package com.example.config.exception

import com.example.exception.BaseException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import kotlinx.serialization.Serializable

fun Application.handler() {
    install(StatusPages) {
        exception<BaseException> { call, cause ->
            call.respond(cause.statusCode, ErrorResponse(cause.statusCode.value, cause.message ?: ""))
        }

        exception<Throwable> { call, cause ->
            val status = HttpStatusCode.InternalServerError
            call.respond(status, ErrorResponse(status.value, status.description, cause.message ?: ""))
        }
    }
}

@Serializable
data class ErrorResponse(val status: Int, val message: String, val details: String = "")
