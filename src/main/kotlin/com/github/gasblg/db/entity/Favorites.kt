package com.github.gasblg.db.entity

import org.jetbrains.exposed.sql.Table

object Favorites : Table() {
    val ticker = varchar("ticker", length = 10)

    override val primaryKey = PrimaryKey(ticker)
}