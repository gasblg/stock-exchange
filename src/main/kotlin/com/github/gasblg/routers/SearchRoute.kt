package com.github.gasblg.routers

import com.github.gasblg.db.SearchSchema
import com.github.gasblg.models.Params.getLang
import com.github.gasblg.models.Params.getPage
import com.github.gasblg.models.Params.getPageSize
import com.github.gasblg.models.Params.getQuery
import com.github.gasblg.models.paging.PageParams
import com.github.gasblg.models.paging.PagedResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Route.searchRoute() {


    val searchSchema: SearchSchema by inject()

    get("/search") {
        val lang = call.parameters.getLang()
        val q = call.parameters.getQuery()
        val page = call.parameters.getPage()
        val size = call.parameters.getPageSize()

        val pageParams = PageParams(page = page, count = size)

        val pagedData = searchSchema.getAll(pageParams = pageParams, q = q, lang = lang)
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