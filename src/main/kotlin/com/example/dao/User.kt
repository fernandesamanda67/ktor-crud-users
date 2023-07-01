package com.example.dao

import com.example.config.serialization.LocalDateSerializer
import com.example.config.serialization.LocalDateTimeSerializer
import com.example.config.serialization.UuidSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class User(
    @Serializable(with = UuidSerializer::class)
    val id: UUID,
    val name: String,
    val email: String,
    val cpf: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthday: LocalDate,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdDate: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val lastModificationDate: LocalDateTime
)

fun User.toResponseDTO() = User(id, name, email, cpf, birthday, createdDate, lastModificationDate)

object Users : Table() {
    val id = uuid("id")
    val name = text("name")
    val email = text("email").uniqueIndex()
    val cpf = text("cpf").uniqueIndex()
    val birthday = date("birthday")
    val createdDate = datetime("created_date")
    val lastModificationDate = datetime("last_modification_date")
}
