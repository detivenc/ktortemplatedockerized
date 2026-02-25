package com.detivenc.github.ktordocker.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.util.AttributeKey
import java.util.Date
import java.util.concurrent.TimeUnit

data class JwtConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String,
    val expirationMs: Long = TimeUnit.HOURS.toMillis(1)
)

val jwtConfigKey = AttributeKey<JwtConfig>("JwtConfig")

fun Application.configureSecurity() {
    val config = JwtConfig(
        secret = environment.config.propertyOrNull("jwt.secret")?.getString()
            ?: System.getenv("JWT_SECRET") ?: "default-secret-change-in-production",
        issuer = environment.config.propertyOrNull("jwt.issuer")?.getString()
            ?: System.getenv("JWT_ISSUER") ?: "ktor-app",
        audience = environment.config.propertyOrNull("jwt.audience")?.getString()
            ?: System.getenv("JWT_AUDIENCE") ?: "ktor-app-users",
        realm = environment.config.propertyOrNull("jwt.realm")?.getString()
            ?: "ktor app"
    )
    attributes.put(jwtConfigKey, config)

    authentication {
        jwt("jwt-auth") {
            realm = config.realm
            verifier(
                JWT.require(Algorithm.HMAC256(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("username").asString() != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}

fun generateToken(config: JwtConfig, username: String): String =
    JWT.create()
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .withClaim("username", username)
        .withExpiresAt(Date(System.currentTimeMillis() + config.expirationMs))
        .sign(Algorithm.HMAC256(config.secret))
