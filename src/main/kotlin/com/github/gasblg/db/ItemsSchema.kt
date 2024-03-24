package com.github.gasblg.db

import com.github.gasblg.db.entity.Items
import com.github.gasblg.ext.dbQuery
import com.github.gasblg.models.items.ItemRequest
import org.jetbrains.exposed.sql.*

class ItemsSchema {

    suspend fun create(item: ItemRequest) = dbQuery {
        Items.insert {
            it[ticker] = item.ticker
            it[nameId] = item.nameId
            it[shortNameId] = item.shortNameId
            it[descriptionId] = item.descriptionId
            it[type] = item.type
            it[market] = item.market
        }[Items.ticker]
    }

}