package com.detivenc.github.ktordocker

import com.detivenc.github.ktordocker.plugins.configureDatabase
import com.detivenc.github.ktordocker.plugins.configureRouting
import com.detivenc.github.ktordocker.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureDatabase()
    configureRouting()
}
