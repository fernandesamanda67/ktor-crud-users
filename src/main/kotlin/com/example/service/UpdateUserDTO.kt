package com.example.service

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserDTO(
    val name: String,
    val email: String,
    val cpf: String,
    val birthday: String
)
