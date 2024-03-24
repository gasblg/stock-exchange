package com.github.gasblg.routers

import com.github.gasblg.db.CandlesSchema
import com.github.gasblg.models.Params.getPage
import com.github.gasblg.models.Params.getPageSize
import com.github.gasblg.models.candles.CandleRequest
import com.github.gasblg.models.paging.PageParams
import com.github.gasblg.models.paging.PagedResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Route.candlesRoute() {

    val candlesSchema: CandlesSchema by inject()


    post("/candles/create") {
        val candleRequest = call.receive<CandleRequest>()
        val response = candlesSchema.create(candleRequest)
        if (response != null) {
            call.respond(HttpStatusCode.Created, response)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    get("/candles") {
        val page = call.parameters.getPage()
        val size = call.parameters.getPageSize()

        val pageParams = PageParams(page = page, count = size)
        val pagedData = candlesSchema.getAll(pageParams = pageParams)

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
}