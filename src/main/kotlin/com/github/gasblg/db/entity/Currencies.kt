package com.github.gasblg.db.entity

import org.jetbrains.exposed.sql.Table

object Currencies : Table() {
    val ticker = varchar("ticker", length = 10)
    val date = varchar("date", length = 10)
    val rate = double("rate")
    val percent = double("percent")

    override val primaryKey = PrimaryKey(ticker)
}