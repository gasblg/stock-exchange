package com.github.gasblg.di

import com.github.gasblg.db.*
import org.koin.dsl.module

val schemaModule = module {

    single<CandlesSchema> {
        CandlesSchema()
    }
    single<CurrenciesSchema> {
        CurrenciesSchema()
    }
    single<DerivativesSchema> {
        DerivativesSchema()
    }
    single<FavoritesSchema> {
        FavoritesSchema()
    }
    single<ItemsSchema> {
        ItemsSchema()
    }
    single {
        LanguagesSchema()
    }
    single<NewsSchema> {
        NewsSchema()
    }
    single<SearchSchema> {
        SearchSchema()
    }
    single<SharesSchema> {
        SharesSchema()
    }
    single<TranslationsSchema> {
        TranslationsSchema()
    }
}
