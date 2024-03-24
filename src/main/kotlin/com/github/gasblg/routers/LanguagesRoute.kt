package com.github.gasblg.routers

import com.github.gasblg.db.LanguagesSchema
import com.github.gasblg.models.Params.getLangId
import com.github.gasblg.models.languages.LanguageRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.languagesRoute() {

    val languagesSchema: LanguagesSchema by inject()

    post("/languages/create") {
        val request = call.receive<LanguageRequest>()
        val response = languagesSchema.create(request)
        call.respond(HttpStatusCode.Created, response)
    }


    get("/languages") {
        val response = languagesSchema.getAll()
        call.respond(HttpStatusCode.OK, response)
    }

    get("/languages/{id}") {
        val id = call.parameters.getLangId()

        val response = languagesSchema.read(id)
        if (response != null) {
            call.respond(HttpStatusCode.OK, response)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}