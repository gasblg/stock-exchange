package com.github.gasblg

import com.github.gasblg.db.connect.AppDatabase
import com.github.gasblg.di.mainModule
import com.github.gasblg.di.schemaModule
import com.github.gasblg.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.inject

import org.koin.ktor.plugin.Koin

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {

    install(Koin) {
        modules(mainModule, schemaModule)
    }

    val database: AppDatabase by inject()
    database.init()

    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureRouting()

}
