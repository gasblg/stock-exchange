package com.github.gasblg.db

import com.github.gasblg.db.entity.Candles
import com.github.gasblg.db.entity.Currencies
import com.github.gasblg.db.entity.Items
import com.github.gasblg.db.entity.Translations
import com.github.gasblg.ext.dbQuery
import com.github.gasblg.models.currencies.CurrencyRequest
import com.github.gasblg.models.currencies.CurrencyResponse
import org.jetbrains.exposed.sql.*


class CurrenciesSchema {

    suspend fun create(currencyRequest: CurrencyRequest) = dbQuery {
        Currencies.insert {
            it[ticker] = currencyRequest.ticker
            it[date] = currencyRequest.date
            it[rate] = currencyRequest.rate
            it[percent] = currencyRequest.percent
        }
    }

    suspend fun getAll(sortColumn: String?, sortOrder: String?, lang: String): List<CurrencyResponse> {
        return try {
            dbQuery {

                val orderBy = when (sortOrder) {
                    "desc" -> SortOrder.DESC
                    else -> SortOrder.ASC
                }

                val column = when (sortColumn) {
                    "ticker" -> Items.ticker
                    "shortname" -> Items.shortNameId
                    "percent" -> Candles.percent
                    else -> Items.ticker
                }

                val name = Translations.alias("name")
                val shortName = Translations.alias("shortName")
                val description = Translations.alias("description")


                Join(
                    Items, Currencies,
                    onColumn = Items.ticker, otherColumn = Currencies.ticker,
                    joinType = JoinType.INNER
                ).join(
                    name,
                    onColumn = Items.nameId,
                    otherColumn = name[Translations.contentId],
                    joinType = JoinType.INNER,
                    additionalConstraint = { name[Translations.language] eq lang }
                ).join(
                    shortName,
                    onColumn = Items.shortNameId,
                    otherColumn = shortName[Translations.contentId],
                    joinType = JoinType.INNER,
                    additionalConstraint = { shortName[Translations.language] eq lang }
                ).join(
                    description,
                    onColumn = Items.descriptionId,
                    otherColumn = description[Translations.contentId],
                    joinType = JoinType.INNER,
                    additionalConstraint = { description[Translations.language] eq lang }
                ).selectAll().orderBy(column, orderBy)
                    .map {
                        CurrencyResponse(
                            ticker = it[Items.ticker],
                            name = it[name[Translations.text]],
                            shortName = it[shortName[Translations.text]],
                            description = it[description[Translations.text]],
                            type = it[Items.type],
                            market = it[Items.market],
                            date = it[Currencies.date],
                            rate = it[Currencies.rate],
                            percent = it[Currencies.percent]
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun read(ticker: String, lang: String): CurrencyResponse? {
        return dbQuery {
            val name = Translations.alias("name")
            val shortName = Translations.alias("shortName")
            val description = Translations.alias("description")


            Join(
                Items, Currencies,
                onColumn = Items.ticker,
                otherColumn = Currencies.ticker,
                joinType = JoinType.INNER
            ).join(
                name,
                onColumn = Items.nameId,
                otherColumn = name[Translations.contentId],
                joinType = JoinType.INNER,
                additionalConstraint = { name[Translations.language] eq lang }
            ).join(
                shortName,
                onColumn = Items.shortNameId,
                otherColumn = shortName[Translations.contentId],
                joinType = JoinType.INNER,
                additionalConstraint = { shortName[Translations.language] eq lang }
            ).join(
                description,
                onColumn = Items.descriptionId,
                otherColumn = description[Translations.contentId],
                joinType = JoinType.INNER,
                additionalConstraint = { description[Translations.language] eq lang }
            ).select { Items.ticker eq ticker }.map {
                CurrencyResponse(
                    ticker = it[Items.ticker],
                    name = it[name[Translations.text]],
                    shortName = it[shortName[Translations.text]],
                    description = it[description[Translations.text]],
                    type = it[Items.type],
                    market = it[Items.market],
                    date = it[Currencies.date],
                    rate = it[Currencies.rate],
                    percent = it[Currencies.percent]
                )
            }
        }.singleOrNull()
    }
}
