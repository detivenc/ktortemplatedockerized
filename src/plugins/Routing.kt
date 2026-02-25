package com.detivenc.github.ktordocker.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val username: String, val password: String)

@Serializable
data class LoginResponse(val token: String)

@Serializable
data class ChatRequest(val message: String)

@Serializable
data class ChatResponse(val response: String)

fun Application.configureRouting() {
    val jwtConfig = attributes[jwtConfigKey]
    val aiConfig = attributes[aiConfigKey]

    routing {
        get("/health") {
            call.respond(HttpStatusCode.OK, mapOf("status" to "UP"))
        }
        get("/") {
            call.respondText("Ktor application is running!")
        }
        post("/auth/login") {
            val request = call.receive<LoginRequest>()
            // Demo: accept any non-empty username/password combination
            if (request.username.isBlank() || request.password.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Username and password are required"))
                return@post
            }
            val token = generateToken(jwtConfig, request.username)
            call.respond(HttpStatusCode.OK, LoginResponse(token))
        }
        authenticate("jwt-auth") {
            get("/secure") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal?.payload?.getClaim("username")?.asString() ?: "unknown"
                call.respond(HttpStatusCode.OK, mapOf("message" to "Hello, $username! This is a protected route."))
            }
            post("/ai/chat") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal?.payload?.getClaim("username")?.asString() ?: "unknown"
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
