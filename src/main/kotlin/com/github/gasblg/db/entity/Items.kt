package com.github.gasblg.db.entity

import org.jetbrains.exposed.sql.Table

object Items : Table() {
    val ticker = varchar("ticker", length = 10)
    val nameId = integer("name_id")
    val shortNameId = integer("short_name_id")
    val descriptionId = integer("description_id")
    val type = varchar("type", length = 10)
    val market = varchar("market", length = 20)

    override val primaryKey = PrimaryKey(ticker)

}