package com.github.gasblg.routers

import com.github.gasblg.db.NewsSchema
import com.github.gasblg.models.Params.getId
import com.github.gasblg.models.Params.getLang
import com.github.gasblg.models.Params.getPage
import com.github.gasblg.models.Params.getPageSize
import com.github.gasblg.models.paging.PagedResponse
import com.github.gasblg.models.news.NewsRequest
import com.github.gasblg.models.paging.PageParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.newsRoute() {

    val newsSchema: NewsSchema by inject()

    post("/news/create") {
        val newsRequest = call.receive<NewsRequest>()
        val response = newsSchema.create(newsRequest)
        call.respond(HttpStatusCode.Created, response)
    }


    get("/news") {
        val lang = call.parameters.getLang()
        val page = call.parameters.getPage()
        val size = call.parameters.getPageSize()

        val pageParams = PageParams(page = page, count = size)
        val pagedData = newsSchema.getAll(pageParams = pageParams, lang = lang)

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

    get("/news/{id}") {
        val id = call.parameters.getId()
        val lang = call.parameters.getLang()

        val response = newsSchema.read(id, lang)
        if (response != null) {
            call.respond(HttpStatusCode.OK, response)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}