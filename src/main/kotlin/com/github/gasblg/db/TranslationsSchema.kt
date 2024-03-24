package com.github.gasblg.db

import com.github.gasblg.db.entity.Translations
import com.github.gasblg.ext.dbQuery
import com.github.gasblg.models.paging.PageParams
import com.github.gasblg.models.paging.PagedData
import com.github.gasblg.models.paging.getData
import com.github.gasblg.models.paging.getEmptyList
import com.github.gasblg.models.translations.TranslationRequest
import com.github.gasblg.models.translations.TranslationResponse
import org.jetbrains.exposed.sql.*


class TranslationsSchema {

    suspend fun create(translation: TranslationRequest): TranslationResponse = dbQuery {
        val t = Translations.insert {
            it[contentId] = translation.contentId
            it[language] = translation.language
            it[text] = translation.text
        }
        TranslationResponse(
            t[Translations.id],
            t[Translations.contentId],
            t[Translations.language],
            t[Translations.text]
        )
    }

    suspend fun getAll(pageParams: PageParams): PagedData<TranslationResponse> {
        return try {
            dbQuery {
                val total = Translations.selectAll().count()

                val list = Translations
                    .selectAll()
                    .limit(pageParams.count, offset = pageParams.offset)
                    .map {
                        TranslationResponse(
                            it[Translations.id],
                            it[Translations.contentId],
                            it[Translations.language],
                            it[Translations.text]
                        )
                    }
                pageParams.getData(list, total)
            }
        } catch (e: Exception) {
            pageParams.getEmptyList()
        }
    }

    suspend fun read(id: Int): TranslationResponse? {
        return dbQuery {
            Translations.select { Translations.id eq id }
                .map {
                    TranslationResponse(
                        it[Translations.id],
                        it[Translations.contentId],
                        it[Translations.language],
                        it[Translations.text]
                    )
                }
                .singleOrNull()
        }
    }
}