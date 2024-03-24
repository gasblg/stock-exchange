package com.github.gasblg.di

import com.github.gasblg.db.connect.AppDatabase
import org.koin.dsl.module

val mainModule = module {
    single {
        AppDatabase()
    }
}
