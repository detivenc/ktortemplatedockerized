package com.detivenc.github.ktordocker.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabase() {
    val dbUrl = environment.config.propertyOrNull("database.url")?.getString()
        ?: System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/postgres"
    val dbUser = environment.config.propertyOrNull("database.user")?.getString()
        ?: System.getenv("DB_USERNAME") ?: "postgres"
    val dbPassword = environment.config.propertyOrNull("database.password")?.getString()
        ?: System.getenv("DB_PASSWORD") ?: "postgres"

    Database.connect(
        url = dbUrl,
        driver = "org.postgresql.Driver",
        user = dbUser,
        password = dbPassword
    )
}
