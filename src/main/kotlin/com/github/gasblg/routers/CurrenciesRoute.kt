package com.github.gasblg.routers

import com.github.gasblg.models.currencies.CurrencyRequest
import com.github.gasblg.db.CurrenciesSchema
import com.github.gasblg.models.Params.getLang
import com.github.gasblg.models.Params.getSortColumn
import com.github.gasblg.models.Params.getSortOrder
import com.github.gasblg.models.Params.getTicker
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Route.currenciesRoute() {

    val currenciesSchema: CurrenciesSchema by inject()

    post("/currencies/create") {
        val currencyRequest = call.receive<CurrencyRequest>()
        val response = currenciesSchema.create(currencyRequest)
        call.respond(HttpStatusCode.Created, response)
    }


    get("/currencies") {
        val lang = call.parameters.getLang()
        val sortColumn = call.parameters.getSortColumn()
        val sortOrder = call.parameters.getSortOrder()

        val response = currenciesSchema.getAll(sortOrder = sortOrder, sortColumn = sortColumn, lang = lang)
        call.respond(HttpStatusCode.OK, response)
    }

    get("/currencies/{ticker}") {
        val lang = call.parameters.getLang()
        val ticker = call.parameters.getTicker()

        val response = currenciesSchema.read(ticker, lang)
        if (response != null) {
            call.respond(HttpStatusCode.OK, response)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}