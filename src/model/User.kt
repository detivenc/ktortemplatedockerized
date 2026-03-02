package model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.core.Table

object Users : Table("users") {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 50).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)
    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class LoginRequest(val username: String, val password: String)

@Serializable
data class LoginResponse(val token: String)

@Serializable
data class ChatRequest(val message: String)

@Serializable
data class ChatResponse(val response: String)

@Serializable
data class RegisterRequest(val username: String, val password: String)
