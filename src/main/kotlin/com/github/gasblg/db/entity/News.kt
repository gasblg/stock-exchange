package com.github.gasblg.db.entity

import org.jetbrains.exposed.sql.Table

object News : Table() {
    val id = integer("id").autoIncrement()
    val titleId = integer("title_id")
    val date = varchar("date", length = 20)
    val bodyId = integer("body_id")

    override val primaryKey = PrimaryKey(id)

 }