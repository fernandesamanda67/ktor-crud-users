package com.example.service

import com.example.dao.User
import com.example.dao.UserDAO
import com.example.dao.toResponseDTO
import com.example.exception.CpfAlreadyExists
import com.example.exception.EmailAlreadyExists
import com.example.exception.InvalidCpf
import com.example.exception.UserNotFound
import com.example.util.isValidCpf
import com.example.util.parseToLocalDateType
import com.example.util.verifyAge
import kotlinx.serialization.ExperimentalSerializationApi
import java.util.UUID

interface UserService {
    fun getAllUsers(name: String?): List<User>
    fun create(user: CreateUserDTO)
    fun getByUUid(id: String): User
    fun update(id: String, values: UpdateUserDTO)
}

@ExperimentalSerializationApi
class UserServiceImpl(private val repository: UserDAO) : UserService {
    override fun getAllUsers(name: String?): List<User> {
        return repository.getAll(name)
    }

    override fun create(user: CreateUserDTO) {
        if (!isValidCpf(user.cpf)) throw InvalidCpf("CPF Invalid")
        verifyAge(parseToLocalDateType(user.birthday))
        verifyEmailAlreadyExists(user.email, "")
        verifyCpfAlreadyExists(user.cpf, "")
        return repository.create(user)
    }

    override fun getByUUid(id: String): User {
        val user = repository.getByUUid(id) ?: throw UserNotFound("User with id $id not found")
        return user.toResponseDTO()
    }

    override fun update(id: String, values: UpdateUserDTO) {
        if (!isValidCpf(values.cpf)) throw InvalidCpf("CPF Invalid")
        verifyAge(parseToLocalDateType(values.birthday))
        verifyEmailAlreadyExists(values.email, id)
        verifyCpfAlreadyExists(values.cpf, id)
        return repository.update(id, values)
    }

    private fun verifyEmailAlreadyExists(email: String, id: String) {
        val result = repository.getByEmail(email)
        if (result != null) {
            if (id.isEmpty()) {
                throw EmailAlreadyExists("Email already exists")
            }
            if (result.id != UUID.fromString(id)) {
                throw EmailAlreadyExists("Email already exists")
            }
        }
    }

    private fun verifyCpfAlreadyExists(cpf: String, id: String) {
        val result = repository.getByCpf(cpf)
        if (result != null) {
            if (id.isEmpty()) {
                throw CpfAlreadyExists("CPF already exists")
            }
            if (result.id != UUID.fromString(id)) {
                throw CpfAlreadyExists("CPF already exists")
            }
        }
    }
}
