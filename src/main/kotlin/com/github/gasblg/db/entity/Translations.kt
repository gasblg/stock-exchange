package com.github.gasblg.db.entity

import org.jetbrains.exposed.sql.Table

object Translations : Table() {
    val id = integer("id").autoIncrement()
    val contentId = integer("content_id")
    val language = varchar("language", length = 2)
    val text = varchar("text", length = 5000)

    override val primaryKey = PrimaryKey(contentId, language)

 }