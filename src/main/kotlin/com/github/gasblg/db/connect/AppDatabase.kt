package com.github.gasblg.db.connect

import org.jetbrains.exposed.sql.Database

class AppDatabase {

    fun init(){
        Database.connect(
            url = "jdbc:postgresql://127.0.0.1:5432/exchange",
            driver = "org.postgresql.Driver",
            user = "",
            password = ""
        )
    }
}