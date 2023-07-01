package com.example.service

import com.example.dao.User
import com.example.dao.UserDAO
import com.example.exception.CpfAlreadyExists
import com.example.exception.EmailAlreadyExists
import com.example.exception.InvalidCpf
import com.example.exception.MinimumAge
import com.example.exception.UserCreateException
import com.example.exception.UserNotFound
import com.example.exception.UserUpdateException
import com.example.util.Util
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertNotNull
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@OptIn(ExperimentalSerializationApi::class)
class UserServiceTest : StringSpec({

    val userRepository: UserDAO = mockk()
    val util: Util = mockk()
    val userService = UserServiceImpl(repository = userRepository, util = util)

    "should list all users" {
        every { userRepository.getAll(any()) } returns listOf(user)

        val result = userService.getAllUsers("")

        assertNotNull(result)
    }

    "should insert a new user" {
        every { util.isValidCpf(any()) } returns true
        every { util.parseToLocalDateType(any()) } returns LocalDate.now()
        every { util.isValidAge(any()) } returns true

        every { userRepository.getByEmail(any()) } returns null
        every { userRepository.getByCpf(any()) } returns null
        every { userRepository.create(any()) } returns Unit

        userService.create(userCreate)

        verify(
            exactly = 1,
            verifyBlock = {
                userRepository.create(any())
            }
        )
    }

    "should throw user create exception" {
        every { util.isValidCpf(any()) } returns true
        every { util.parseToLocalDateType(any()) } returns LocalDate.now()
        every { util.isValidAge(any()) } returns true

        every { userRepository.getByEmail(any()) } returns null
        every { userRepository.getByCpf(any()) } returns null
        every { userRepository.create(any()) } throws UserCreateException("")

        assertThrows<UserCreateException> {
            userService.create(userCreate)
        }
    }

    "should throw invalid cpf exception" {
        every { util.isValidCpf(any()) } returns false
        every { util.parseToLocalDateType(any()) } returns LocalDate.now()
        every { util.isValidAge(any()) } returns true

        every { userRepository.getByEmail(any()) } returns null
        every { userRepository.getByCpf(any()) } returns null
        every { userRepository.create(any()) } returns Unit

        assertThrows<InvalidCpf> {
            userService.create(userCreate)
        }
    }

    "should throw minimum age exception" {
        every { util.isValidCpf(any()) } returns true
        every { util.parseToLocalDateType(any()) } returns LocalDate.now()
        every { util.isValidAge(any()) } returns false

        every { userRepository.getByEmail(any()) } returns null
        every { userRepository.getByCpf(any()) } returns null
        every { userRepository.create(any()) } returns Unit

        assertThrows<MinimumAge> {
            userService.create(userCreate)
        }
    }

    "should throw email already exists exception" {
        every { util.isValidCpf(any()) } returns true
        every { util.parseToLocalDateType(any()) } returns LocalDate.now()
        every { util.isValidAge(any()) } returns true

        every { userRepository.getByEmail(any()) } returns user
        every { userRepository.getByCpf(any()) } returns null
        every { userRepository.create(any()) } returns Unit

        assertThrows<EmailAlreadyExists> {
            userService.create(userCreate)
        }
    }

    "should throw cpf already exists exception" {
        every { util.isValidCpf(any()) } returns true
        every { util.parseToLocalDateType(any()) } returns LocalDate.now()
        every { util.isValidAge(any()) } returns true

        every { userRepository.getByEmail(any()) } returns null
        every { userRepository.getByCpf(any()) } returns user
        every { userRepository.create(any()) } returns Unit

        assertThrows<CpfAlreadyExists> {
            userService.create(userCreate)
        }
    }

    "should get user by id" {
        every { userRepository.getByUUid(any()) } returns user

        val result = userService.getByUUid(user.id.toString())

        assertNotNull(result)
    }

    "should throw user id not found" {
        every { userRepository.getByUUid(any()) } returns null

        assertThrows<UserNotFound> {
            userService.getByUUid(UUID.randomUUID().toString())
        }
    }

    "should update a user" {
        every { util.isValidCpf(any()) } returns true
        every { util.parseToLocalDateType(any()) } returns LocalDate.now()
        every { util.isValidAge(any()) } returns true

        every { userRepository.getByEmail(any()) } returns null
        every { userRepository.getByCpf(any()) } returns null
        every { userRepository.create(any()) } returns Unit
        every { userRepository.getByUUid(any()) } returns User(
            id = userUuid,
            name = "Maria",
            email = "maria@teste.com",
            cpf = "12400735000",
            birthday = LocalDate.now(),
            createdDate = LocalDateTime.now(),
            lastModificationDate = LocalDateTime.now()
        )

        userService.create(userCreate)
        val userCreated = userService.getByUUid(userUuid.toString())

        every { userRepository.update(any(), any()) } returns Unit

        val userUpdated = userService.update(userCreated.id.toString(), userUpdate)

        assertNotNull(userUpdated)
    }

    "should throw user update exception" {
        every { util.isValidCpf(any()) } returns true
        every { util.parseToLocalDateType(any()) } returns LocalDate.now()
        every { util.isValidAge(any()) } returns true

        every { userRepository.getByEmail(any()) } returns null
        every { userRepository.getByCpf(any()) } returns null
        every { userRepository.create(any()) } returns Unit
        every { userRepository.getByUUid(any()) } returns User(
            id = userUuid,
            name = "Maria",
            email = "maria@teste.com",
            cpf = "12400735000",
            birthday = LocalDate.now(),
            createdDate = LocalDateTime.now(),
            lastModificationDate = LocalDateTime.now()
        )

        userService.create(userCreate)
        val userCreated = userService.getByUUid(userUuid.toString())

        every { userRepository.update(any(), any()) } throws UserUpdateException("")

        assertThrows<UserUpdateException> {
            userService.update(userCreated.id.toString(), userUpdate)
        }
    }
}) {
    companion object {
        private val userUuid: UUID = UUID.randomUUID()

        val user = User(
            id = userUuid,
            name = "Maria",
            email = "maria@teste.com",
            cpf = "12400735000",
            birthday = LocalDate.now(),
            createdDate = LocalDateTime.now(),
            lastModificationDate = LocalDateTime.now()
        )

        val userCreate = CreateUserDTO(
            name = "Maria",
            email = "maria@teste.com",
            cpf = "12400735000",
            birthday = "20.02.2000"
        )

        val userUpdate = UpdateUserDTO(
            name = "Ana",
            email = "maria@teste.com",
            cpf = "12400735000",
            birthday = "20.02.2000"
        )
    }
}
