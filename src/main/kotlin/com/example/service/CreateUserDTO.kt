package com.example.service

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserDTO(
    val name: String,
    val email: String,
    val cpf: String,
    val birthday: String
)
