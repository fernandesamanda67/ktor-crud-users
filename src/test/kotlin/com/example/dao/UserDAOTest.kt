package com.example.dao

import com.example.TestDatabaseFactory
import com.example.service.CreateUserDTO
import com.example.service.UpdateUserDTO
import com.example.util.Util
import io.kotest.core.spec.style.StringSpec
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import java.util.UUID

class UserDAOTest : StringSpec({
    val database = TestDatabaseFactory()
    val util = Util()
    val repository = UserDAOImpl(util)

    beforeTest {
        database.connect()
    }

    "should get users" {
        assertNotNull(repository.getAll(""))
    }

    "should get no user by id" {
        assertNull(repository.getByUUid(UUID.randomUUID().toString()))
    }

    "should create a user" {
        repository.create(createUser())

        val savedUser = repository.getAll("Ana")

        assertNotNull(savedUser)
    }

    "should update a user" {
        repository.create(createUser())

        val savedUser = repository.getAll("Ana")

        val updateUser = UpdateUserDTO("Maria", "teste@teste.com", "39933749080", "11.08.1987")

        repository.update(savedUser[0].id.toString(), updateUser)

        val updatedUser = repository.getAll("Maria")

        assertNotNull(updatedUser)
        assertEquals(updateUser.name, updatedUser[0].name)
    }

    "should get user by email" {
        repository.create(createUser())

        assertNotNull(repository.getByEmail("teste@teste.com"))
    }

    "should get user by cpf" {
        repository.create(createUser())

        assertNotNull(repository.getByCpf("39933749080"))
    }

    afterEach {
        database.close()
        database.connect()
    }
}) {
    companion object {
        fun createUser() = CreateUserDTO("Ana", "teste@teste.com", "39933749080", "11.08.1987")
    }
}
