package com.github.gasblg.routers

import com.github.gasblg.db.ItemsSchema
import com.github.gasblg.models.items.ItemRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Route.itemsRoute() {

    val itemsSchema: ItemsSchema by inject()

    post("/items/create") {
        val itemRequest = call.receive<ItemRequest>()
        val response = itemsSchema.create(itemRequest)
        call.respond(HttpStatusCode.Created, response)
    }


}