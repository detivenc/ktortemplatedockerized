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

    val dbUrl = config.propertyOrNull("database.url")?.getString()
        ?: System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/postgres"
    val dbUser = config.propertyOrNull("database.user")?.getString()
        ?: System.getenv("DB_USERNAME") ?: "postgres"
    val dbPassword = config.propertyOrNull("database.password")?.getString()
        ?: System.getenv("DB_PASSWORD") ?: "postgres"
    val dbDriver = config.propertyOrNull("database.driver")?.getString()
        ?: System.getenv("DB_DRIVER") ?: "org.postgresql.Driver"

    try {
        Database.connect(
            url = dbUrl,
            driver = dbDriver,
            user = dbUser,
            password = dbPassword
        )
        logger.info("Successfully connected to database at $dbUrl")
    } catch (e: Exception) {
        logger.error("Failed to connect to database: ${e.message}")
        // Consider if application should fail to start if DB connection is critical
    }

    transaction {
        SchemaUtils.create(Users)
    }
}