package com.example.config.dependencyinjection

import com.example.config.serialization.jsonConfig
import com.example.dao.UserDAO
import com.example.dao.UserDAOImpl
import com.example.service.UserService
import com.example.service.UserServiceImpl
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.dsl.module

/*fun utilsModule() = module {
    single { jsonConfig() }
}*/

fun repositoryModules() = module {
    single<UserDAO> { UserDAOImpl() }
}

@ExperimentalSerializationApi
fun serviceModules() = module {
    single<UserService> { UserServiceImpl(get()) }
}
