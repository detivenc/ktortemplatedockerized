package plugins

import Users
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

fun Application.configureDatabase() {
    val logger = LoggerFactory.getLogger("Database")
    val config = environment.config

    // Prefer config/env, but pick a dependency-safe default (H2) for tests/dev
    val dbUrl = config.propertyOrNull("database.url")?.getString()
        ?: System.getenv("DB_URL")
        ?: "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL"

    val dbUser = config.propertyOrNull("database.user")?.getString()
        ?: System.getenv("DB_USERNAME")
        ?: "sa"

    val dbPassword = config.propertyOrNull("database.password")?.getString()
        ?: System.getenv("DB_PASSWORD")
        ?: ""

    val dbDriver = config.propertyOrNull("database.driver")?.getString()
        ?: System.getenv("DB_DRIVER")
        ?: "org.h2.Driver"

    val db = try {
        Database.connect(
            url = dbUrl,
            driver = dbDriver,
            user = dbUser,
            password = dbPassword
        ).also {
            logger.info("Successfully connected to database at $dbUrl (driver=$dbDriver)")
        }
    } catch (e: Exception) {
        logger.error("Failed to connect to database (driver=$dbDriver, url=$dbUrl): ${e.message}", e)
        return
    }

    transaction(db) {
        SchemaUtils.create(Users)
    }
}