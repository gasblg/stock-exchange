package com.github.gasblg.plugins

import com.github.gasblg.routers.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }
    }

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }

    routing {
        currenciesRoute()
        newsRoute()
        itemsRoute()
        sharesRoute()
        candlesRoute()
        favoritesRoute()
        searchRoute()
        derivativesRoute()
        translationsRoute()
        languagesRoute()
    }
}
