package com.github.gasblg.routers

import com.github.gasblg.db.DerivativesSchema
import com.github.gasblg.models.Params.getLang
import com.github.gasblg.models.Params.getSortColumn
import com.github.gasblg.models.Params.getSortOrder
import com.github.gasblg.models.Params.getTicker
import com.github.gasblg.models.derivatives.DerivativesRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Route.derivativesRoute() {

    val derivativesSchema: DerivativesSchema by inject()


    post("/derivatives/create") {
        val derivativesRequest = call.receive<DerivativesRequest>()
        val response = derivativesSchema.create(derivativesRequest)
        call.respond(HttpStatusCode.Created, response)
    }


    get("/derivatives") {
        val lang = call.parameters.getLang()
        val sortColumn = call.parameters.getSortColumn()
        val sortOrder = call.parameters.getSortOrder()

        val response = derivativesSchema.getAll(sortOrder = sortOrder, sortColumn = sortColumn, lang = lang)
        call.respond(HttpStatusCode.OK, response)
    }

    get("/derivatives/{ticker}") {
        val lang = call.parameters.getLang()
        val ticker = call.parameters.getTicker()

        val response = derivativesSchema.read(ticker, lang)
        if (response != null) {
            call.respond(HttpStatusCode.OK, response)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}