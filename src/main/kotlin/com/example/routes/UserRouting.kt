package com.example.routes

import com.example.service.CreateUserDTO
import com.example.service.UpdateUserDTO
import com.example.service.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.util.getOrFail
import org.koin.ktor.ext.inject

fun Route.userRouting() {
    val userService by inject<UserService>()

    route("users") {
        get {
            var name = ""
            if (!call.request.queryParameters["name"].isNullOrEmpty()) {
                name = call.request.queryParameters["name"].toString()
            }
            call.respond(userService.getAllUsers(name))
        }
        get("/{id}") {
            val userId = call.parameters["id"]!!
            val user = userService.getByUUid(userId)
            call.respond(user)
        }
        post {
            val user = call.receive<CreateUserDTO>()
            userService.create(user)
            call.respond(HttpStatusCode.Created)
        }
        put("/{id}") {
            val userId = call.parameters.getOrFail<Int>("id").toString()
            val values = call.receive<UpdateUserDTO>()
            userService.update(userId, values)
            call.respond(HttpStatusCode.NoContent)
        }
        //     val id = call.parameters.getOrFail<Int>("id").toInt()
    }
}
