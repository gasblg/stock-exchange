package com.github.gasblg.db

import com.github.gasblg.db.entity.Languages
import com.github.gasblg.ext.dbQuery
import com.github.gasblg.models.languages.LanguageRequest
import com.github.gasblg.models.languages.LanguageResponse
import org.jetbrains.exposed.sql.*

class LanguagesSchema {

    suspend fun create(languageRequest: LanguageRequest) = dbQuery {
        Languages.insert {
            it[id] = languageRequest.id
            it[name] = languageRequest.name
        }[Languages.id]
    }

    suspend fun getAll(): List<LanguageResponse> {
        return try {
            dbQuery {
                Languages.selectAll()
                    .map {
                        LanguageResponse(
                            id = it[Languages.id],
                            name = it[Languages.name]
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun read(id: String): LanguageResponse? {
        return dbQuery {
            Languages.select { Languages.id eq id }
                .map { LanguageResponse(it[Languages.id], it[Languages.name]) }
                .singleOrNull()
        }
    }
}