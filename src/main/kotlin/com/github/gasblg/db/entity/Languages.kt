package com.github.gasblg.db.entity

import org.jetbrains.exposed.sql.Table

object Languages : Table() {
    val id = varchar("id", length = 2)
    val name = varchar("name", length = 20)

    override val primaryKey = PrimaryKey(id)

 }