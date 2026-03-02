package service

import config.security.JwtConfig
import config.security.generateToken
import model.LoginRequest
import model.RegisterRequest
import model.Users
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

class AuthService(private val jwtConfig: JwtConfig) {

    fun login(request: LoginRequest): String? {
        val userHash = fetchPasswordHashByUsername(request.username)
        val ok = userHash != null && BCrypt.checkpw(request.password, userHash)
        if (!ok) return null

        return generateToken(jwtConfig, request.username)
    }

    fun register(request: RegisterRequest): Boolean {
        val hash = BCrypt.hashpw(request.password, BCrypt.gensalt())
        return tryCreateUser(request.username, hash)
    }

    private fun fetchPasswordHashByUsername(user: String): String? = transaction {
        Users.selectAll()
            .where { Users.username eq user }
            .limit(1)
            .firstOrNull()
            ?.get(Users.passwordHash)
    }

    fun fetchUserIdByUsername(user: String): Int? = transaction {
        Users.selectAll()
            .where { Users.username eq user }
            .limit(1)
            .firstOrNull()
            ?.get(Users.id)
    }

    private fun tryCreateUser(user: String, hash: String): Boolean = try {
        transaction {
            Users.insert {
                it[username] = user
                it[passwordHash] = hash
            }
        }
        true
    } catch (_: Exception) {
        false
    }
}
