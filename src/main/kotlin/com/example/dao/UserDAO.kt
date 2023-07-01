package com.example.dao

import com.example.exception.UserCreateException
import com.example.exception.UserUpdateException
import com.example.service.CreateUserDTO
import com.example.service.UpdateUserDTO
import com.example.util.Util
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID

interface UserDAO {
    fun getAll(name: String?): List<User>
    fun create(user: CreateUserDTO)
    fun getByUUid(id: String): User?
    fun update(id: String, values: UpdateUserDTO)
    fun getByEmail(email: String): User?
    fun getByCpf(cpf: String): User?
}

class UserDAOImpl(private val util: Util) : UserDAO {

    private fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        name = row[Users.name],
        email = row[Users.email],
        cpf = row[Users.cpf],
        birthday = row[Users.birthday],
        createdDate = row[Users.createdDate],
        lastModificationDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    )

    override fun getAll(name: String?): List<User> {
        return transaction {
            if (name.isNullOrBlank()) {
                Users.selectAll().map(::resultRowToUser).toList()
            }
            Users.select(Users.name like "$name%").map(::resultRowToUser).toList()
        }
    }

    override fun create(user: CreateUserDTO) {
        try {
            transaction {
                Users.insert {
                    it[id] = UUID.randomUUID()
                    it[name] = user.name
                    it[email] = user.email
                    it[cpf] = user.cpf
                    it[birthday] = util.parseToLocalDateType(user.birthday)
                    it[createdDate] = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
                    it[lastModificationDate] = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
                }
            }
        } catch (e: ExposedSQLException) {
            throw UserCreateException("Error creating user")
        }
    }

    override fun getByUUid(id: String): User? {
        return transaction {
            Users
                .select { Users.id eq UUID.fromString(id) }
                .map(::resultRowToUser).singleOrNull()
        }
    }

    override fun update(id: String, values: UpdateUserDTO) {
        try {
            transaction {
                Users.update({ Users.id eq UUID.fromString(id) }) {
                    it[name] = values.name
                    it[email] = values.email
                    it[cpf] = values.cpf
                    it[birthday] = util.parseToLocalDateType(values.birthday)
                    it[lastModificationDate] = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
                }
            }
        } catch (e: ExposedSQLException) {
            throw UserUpdateException("Error updating user")
        }
    }

    override fun getByEmail(email: String): User? {
        return transaction {
            Users
                .select { Users.email eq email }
                .map(::resultRowToUser).singleOrNull()
        }
    }

    override fun getByCpf(cpf: String): User? {
        return transaction {
            Users
                .select { Users.cpf eq cpf }
                .map(::resultRowToUser).singleOrNull()
        }
    }
}
