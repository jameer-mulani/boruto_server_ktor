package com.example

import com.example.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused")
fun Application.module() {

    //koin must be the first one to call
    configureKoin()
    configureSerialization() //content-negotiation
    configureMonitoring()    //call logging
    configureRouting()
    configureDefaultHeaders()
    configureStatusPages()
}
