import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import plugins.configureAI
import plugins.configureDatabase
import plugins.configureRouting
import plugins.configureSecurity
import plugins.configureSerialization

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

