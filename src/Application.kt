import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import config.ai.configureAI
import repository.configureDatabase
import routing.configureRouting
import config.security.configureSecurity
import config.serialization.configureSerialization

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureSecurity()
    configureAI()
    configureDatabase()
    configureRouting()
}

