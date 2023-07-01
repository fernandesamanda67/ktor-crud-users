val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val postgresVersion: String by project
val h2Version: String by project
val exposedVersion: String by project
val hikariVersion: String by project
val koinVersion: String by project
val junitVersion: String by project
val kotestVersion: String by project
val mockkVersion: String by project
val jacocoVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.serialization") version "1.8.21"
    id("io.ktor.plugin") version "2.3.1"
    id("jacoco")
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("com.example.ApplicationKt")
    project.setProperty("mainClassName", mainClass.get())

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

jacoco {
    toolVersion = jacocoVersion
}

repositories {
    mavenCentral()
}

fun DependencyHandlerScope.ktor() {
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
}

fun DependencyHandlerScope.koin() {
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-ktor:$koinVersion")
}

fun DependencyHandlerScope.database() {
    implementation("com.zaxxer:HikariCP:$hikariVersion")
    implementation("org.postgresql:postgresql:$postgresVersion")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("com.h2database:h2:$h2Version")
}

fun DependencyHandlerScope.test() {
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.insert-koin:koin-test:$koinVersion")
    testImplementation("com.h2database:h2:$h2Version")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
}

dependencies {
    ktor()
    koin()
    database()
    test()

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
}

tasks {
    test {
        // Use the built-in JUnit support of Gradle.
        useJUnitPlatform()
        testLogging {
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
        finalizedBy(jacocoTestReport)
    }
    jacocoTestReport {
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "com.example.ApplicationKt"))
        }
    }
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjvm-default=all")
            allWarningsAsErrors = false
            jvmTarget = "17"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
}
