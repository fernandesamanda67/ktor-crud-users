package com.example.config.dependencyinjection

import com.example.dao.UserDAO
import com.example.dao.UserDAOImpl
import com.example.service.UserService
import com.example.service.UserServiceImpl
import com.example.util.Util
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.dsl.module

fun repositoryModules() = module {
    single<UserDAO> { UserDAOImpl(get()) }
}

@ExperimentalSerializationApi
fun serviceModules() = module {
    single<UserService> { UserServiceImpl(get(), get()) }
}

fun utilModules() = module {
    single { Util() }
}
