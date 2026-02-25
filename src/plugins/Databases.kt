package com.detivenc.github.ktordocker.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.slf4j.LoggerFactory

fun Application.configureDatabase() {
    val logger = LoggerFactory.getLogger("Database")
    val config = environment.config

    val dbUrl = config.propertyOrNull("database.url")?.getString()
        ?: System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/postgres"
    val dbUser = config.propertyOrNull("database.user")?.getString()
        ?: System.getenv("DB_USERNAME") ?: "postgres"
    val dbPassword = config.propertyOrNull("database.password")?.getString()
        ?: System.getenv("DB_PASSWORD") ?: "postgres"

    try {
        Database.connect(
            url = dbUrl,
            driver = "org.postgresql.Driver",
            user = dbUser,
            password = dbPassword
        )
        logger.info("Successfully connected to database at $dbUrl")
    } catch (e: Exception) {
        logger.error("Failed to connect to database: ${e.message}")
        // Consider if application should fail to start if DB connection is critical
    }
}
