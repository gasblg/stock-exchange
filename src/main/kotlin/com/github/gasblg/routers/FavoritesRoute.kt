package com.github.gasblg.routers

import com.github.gasblg.db.FavoritesSchema
import com.github.gasblg.models.Params.getLang
import com.github.gasblg.models.Params.getLast
import com.github.gasblg.models.Params.getPage
import com.github.gasblg.models.Params.getPageSize
import com.github.gasblg.models.Params.getSortColumn
import com.github.gasblg.models.Params.getSortOrder
import com.github.gasblg.models.Params.getTicker
import com.github.gasblg.models.paging.PageParams
import com.github.gasblg.models.paging.PagedResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.favoritesRoute() {

    val favoritesSchema: FavoritesSchema by inject()

    post("/favorites/{ticker}") {
        val lang = call.parameters.getLang()
        val ticker = call.parameters.getTicker()

        val response = favoritesSchema.create(ticker, lang)
        if (response != null) {
            call.respond(HttpStatusCode.Created, response)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    delete("/favorites/{ticker}") {
        val ticker = call.parameters.getTicker()
        favoritesSchema.delete(ticker)
        call.respond(HttpStatusCode.OK, ticker)
    }


    get("/favorites") {
        val lang = call.parameters.getLang()
        val page = call.parameters.getPage()
        val size = call.parameters.getPageSize()
        val sortColumn = call.parameters.getSortColumn()
        val sortOrder = call.parameters.getSortOrder()

        val pageParams = PageParams(page = page, count = size)

        val pagedData = favoritesSchema.getAll(
            pageParams = pageParams,
            sortColumn = sortColumn,
            sortOrder = sortOrder,
            lang = lang
        )

        val pagedResponse = PagedResponse(
            results = pagedData.items,
            next = pagedData.next,
            previous = pagedData.previous,
            count = pagedData.total
        )

        call.respond(
            HttpStatusCode.OK,
            pagedResponse
        )
    }

    get("/favorites/{ticker}") {
        val lang = call.parameters.getLang()
        val ticker = call.parameters.getTicker()
        val last = call.parameters.getLast()

        val response = favoritesSchema.read(ticker, last, lang)

        if (response != null) {
            call.respond(HttpStatusCode.OK, response)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}