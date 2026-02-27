package plugins

import Users
import Users.passwordHash
import Users.username
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

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

fun Application.configureRouting() {
    val jwtConfig = attributes[JwtConfigKey]
    val aiConfig = attributes[AIConfigKey]

    routing {
        get("/health") {
            call.respond(HttpStatusCode.OK, mapOf("status" to "UP"))
        }

        get("/") {
            call.respondText("Ktor application is running!")
        }

        route("/auth") {
            post("/login") {
                val request = call.receive<LoginRequest>()
                // Demo: accept any non-empty username/password combination
                if (request.username.isBlank() || request.password.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Username and password are required"))
                    return@post
                }
                val userHash: String? = transaction {
                    Users.selectAll()
                        .where { username eq request.username }
                        .limit(1)
                        .firstOrNull()
                        ?.get(passwordHash)
                }

                if (userHash == null || !BCrypt.checkpw(request.password, userHash)) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid credentials"))
                    return@post
                }

                val token = generateToken(jwtConfig, request.username)
                call.respond(HttpStatusCode.OK, LoginResponse(token))
            }

            post("/register") {
                val request = call.receive<RegisterRequest>()

                if (request.username.isBlank() || request.password.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Username and password are required"))
                    return@post
                }

                val hash = BCrypt.hashpw(request.password, BCrypt.gensalt())

                val created = try {
                    transaction {
                        Users.insert {
                            it[username] = request.username
                            it[passwordHash] = hash
                        }
                        true
                    }
                } catch (e: Exception) {
                    false
                }

                if (!created) {
                    call.respond(HttpStatusCode.Conflict, mapOf("error" to "Username already exists"))
                    return@post
                }

                call.respond(HttpStatusCode.Created, mapOf("status" to "registered"))
            }
        }

        authenticate("jwt-auth") {
            get("/secure") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal?.payload?.getClaim("username")?.asString() ?: "unknown"
                call.respond(HttpStatusCode.OK, mapOf("message" to "Hello, $username! This is a protected route."))
            }

            post("/ai/chat") {
                val request = call.receive<ChatRequest>()
                if (request.message.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Message cannot be empty"))
                    return@post
                }
                val response = chatWithAI(aiConfig, request.message)
                call.respond(HttpStatusCode.OK, ChatResponse(response))
            }
        }
    }
}
