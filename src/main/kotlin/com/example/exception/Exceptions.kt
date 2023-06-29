package com.example.exception

import io.ktor.http.HttpStatusCode

abstract class BaseException(message: String, httpStatusCode: HttpStatusCode, cause: Throwable? = null) :
    RuntimeException(message, cause) {
    val statusCode = httpStatusCode
}

class UserNotFound(message: String) : BaseException(message, HttpStatusCode.NotFound)

class MinimumAge(message: String) : BaseException(message, HttpStatusCode.InternalServerError)

class EmailAlreadyExists(message: String) : BaseException(message, HttpStatusCode.InternalServerError)

class CpfAlreadyExists(message: String) : BaseException(message, HttpStatusCode.InternalServerError)

class UserCreateException(message: String) : BaseException(message, HttpStatusCode.InternalServerError)

class InvalidCpf(message: String) : BaseException(message, HttpStatusCode.InternalServerError)