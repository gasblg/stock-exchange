package com.github.gasblg.db.entity

import org.jetbrains.exposed.sql.Table

object Derivatives : Table() {
    val ticker = varchar("ticker", length = 10)
    val firstTrade = varchar("first_trade", length = 10)
    val lastTrade = varchar("last_trade", length = 10)
    val date = varchar("date", length = 10)
    val contractTypeId = integer("contract_type_id")

    override val primaryKey = PrimaryKey(ticker)

}