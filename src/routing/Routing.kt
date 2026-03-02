package routing

import config.ai.AIConfigKey
import config.security.JwtConfigKey
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.*
import service.AIService
import service.AuthService
import service.PostService

private suspend fun ApplicationCall.respondError(status: HttpStatusCode, message: String) {
    respond(status, mapOf("error" to message))
}

private fun ApplicationCall.jwtUsernameOrNull(): String? =
    principal<JWTPrincipal>()?.payload?.getClaim("username")?.asString()

private suspend fun ApplicationCall.requireIntParameter(
    name: String,
    errorMessage: String
): Int? {
    val value = parameters[name]?.toIntOrNull()
    if (value == null) {
        respondError(HttpStatusCode.BadRequest, errorMessage)
        return null
    }
    return value
}

private suspend fun ApplicationCall.requireUserId(authService: AuthService): Int? {
    val username = jwtUsernameOrNull()
    val userId = username?.let(authService::fetchUserIdByUsername)
    if (userId == null) {
        respondError(HttpStatusCode.Unauthorized, "User not found")
        return null
    }
    return userId
}

private fun LoginRequest.hasBlankCredentials(): Boolean =
    username.isBlank() || password.isBlank()

private fun RegisterRequest.hasBlankCredentials(): Boolean =
    username.isBlank() || password.isBlank()

fun Application.configureRouting() {
    val jwtConfig = attributes[JwtConfigKey]
    val aiConfig = attributes[AIConfigKey]

    val authService = AuthService(jwtConfig)
    val aiService = AIService(aiConfig)
    val postService = PostService()

    routing {
        get("/health") {
            call.respond(HttpStatusCode.OK, mapOf("status" to HealthStatus.UP))
        }

        get("/") {
            call.respondText("Ktor application is running!")
        }

        route("/auth") {
            post("/login") {
                val request = call.receive<LoginRequest>()

                if (request.hasBlankCredentials()) {
                    call.respondError(HttpStatusCode.BadRequest, Errors.MISSING_CREDENTIALS)
                    return@post
                }

                val token = authService.login(request)
                if (token == null) {
                    call.respondError(HttpStatusCode.Unauthorized, Errors.INVALID_CREDENTIALS)
                    return@post
                }

                call.respond(HttpStatusCode.OK, LoginResponse(token))
            }

            post("/register") {
                val request = call.receive<RegisterRequest>()

                if (request.hasBlankCredentials()) {
                    call.respondError(HttpStatusCode.BadRequest, Errors.MISSING_CREDENTIALS)
                    return@post
                }

                val isCreated = authService.register(request)
                if (!isCreated) {
                    call.respondError(HttpStatusCode.Conflict, Errors.USERNAME_EXISTS)
                    return@post
                }

                call.respond(HttpStatusCode.Created, mapOf("status" to AuthStatus.REGISTERED))
            }
        }

        route("/posts") {
            get {
                call.respond(HttpStatusCode.OK, postService.getAllPosts())
            }

            get("/{id}") {
                val id = call.requireIntParameter("id", Errors.INVALID_POST_ID) ?: return@get
                val post = postService.getPostById(id)
                if (post == null) {
                    call.respondError(HttpStatusCode.NotFound, Errors.POST_NOT_FOUND)
                    return@get
                }
                call.respond(HttpStatusCode.OK, post)
            }
        }

        authenticate("jwt-auth") {
            get("/secure") {
                val username = call.jwtUsernameOrNull() ?: "unknown"
                call.respond(
                    HttpStatusCode.OK,
                    mapOf("message" to "Hello, $username! This is a protected route.")
                )
            }

            post("/ai/chat") {
                val request = call.receive<ChatRequest>()

                if (request.message.isBlank()) {
                    call.respondError(HttpStatusCode.BadRequest, Errors.EMPTY_MESSAGE)
                    return@post
                }

                val response = aiService.chat(request.message)
                call.respond(HttpStatusCode.OK, ChatResponse(response))
            }

            route("/posts") {
                post {
                    val request = call.receive<PostRequest>()
                    val userId = call.requireUserId(authService) ?: return@post

                    val postId = postService.createPost(request, userId)
                    call.respond(HttpStatusCode.Created, mapOf("id" to postId))
                }

                put("/{id}") {
                    val id = call.requireIntParameter("id", Errors.INVALID_POST_ID) ?: return@put
                    val request = call.receive<PostRequest>()
                    val userId = call.requireUserId(authService) ?: return@put

                    val updated = postService.updatePost(id, request, userId)
                    if (!updated) {
                        call.respondError(HttpStatusCode.NotFound, Errors.POST_NOT_FOUND_OR_UNAUTHORIZED)
                        return@put
                    }
                    call.respond(HttpStatusCode.OK, mapOf("status" to PostStatus.UPDATED))
                }

                delete("/{id}") {
                    val id = call.requireIntParameter("id", Errors.INVALID_POST_ID) ?: return@delete
                    val userId = call.requireUserId(authService) ?: return@delete

                    val deleted = postService.deletePost(id, userId)
                    if (!deleted) {
                        call.respondError(HttpStatusCode.NotFound, Errors.POST_NOT_FOUND_OR_UNAUTHORIZED)
                        return@delete
                    }
                    call.respond(HttpStatusCode.OK, mapOf("status" to PostStatus.DELETED))
                }
            }
        }
    }
}

