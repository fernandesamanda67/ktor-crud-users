package com.example.dao

import com.example.config.serialization.LocalDateSerializer
import com.example.config.serialization.UuidSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate
import java.util.UUID

@Serializable
data class User(
    @Serializable(with = UuidSerializer::class)
    val id: UUID,
    val name: String,
    val email: String,
    val cpf: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthday: LocalDate
)

fun User.toResponseDTO() = User(id, name, email, cpf, birthday)

object Users : Table() {
    val id = uuid("id")
    val name = text("name")
    val email = text("email").uniqueIndex()
    val cpf = text("cpf").uniqueIndex()
    val birthday = date("birthday")
}
