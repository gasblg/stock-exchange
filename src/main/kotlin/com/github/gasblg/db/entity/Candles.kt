package com.github.gasblg.db.entity

import org.jetbrains.exposed.sql.*

object Candles : Table() {
    val ticker = varchar("ticker", length = 10)
    val date = varchar("date", length = 10)
    val open = double("open")
    val low = double("low")
    val high = double("high")
    val close = double("close")
    val volume = integer("volume")
    val price = double("price")
    val percent = double("percent")

    override val primaryKey = PrimaryKey(ticker, date)
}