package com.github.gasblg.routers

import com.github.gasblg.db.TranslationsSchema
import com.github.gasblg.models.Params.getId
import com.github.gasblg.models.Params.getPage
import com.github.gasblg.models.Params.getPageSize
import com.github.gasblg.models.paging.PageParams
import com.github.gasblg.models.paging.PagedResponse
import com.github.gasblg.models.translations.TranslationRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Route.translationsRoute() {

    val translationsSchema: TranslationsSchema by inject()

    post("/translations/create") {
        val request = call.receive<TranslationRequest>()
        val response = translationsSchema.create(request)
        call.respond(HttpStatusCode.Created, response)
    }


    get("/translations") {
        val page = call.parameters.getPage()
        val size = call.parameters.getPageSize()

        val pageParams = PageParams(page = page, count = size)
        val pagedData = translationsSchema.getAll(pageParams = pageParams)

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

    get("/translations/{id}") {
        val id = call.parameters.getId()
        val response = translationsSchema.read(id)
        if (response != null) {
            call.respond(HttpStatusCode.OK, response)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}