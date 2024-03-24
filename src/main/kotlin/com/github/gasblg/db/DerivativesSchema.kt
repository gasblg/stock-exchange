package com.github.gasblg.db

import com.github.gasblg.db.entity.Candles
import com.github.gasblg.db.entity.Derivatives
import com.github.gasblg.db.entity.Items
import com.github.gasblg.db.entity.Translations
import com.github.gasblg.ext.dbQuery
import com.github.gasblg.models.derivatives.DerivativesRequest
import com.github.gasblg.models.derivatives.DerivativesResponse
import org.jetbrains.exposed.sql.*

class DerivativesSchema {

    suspend fun create(derivativesRequest: DerivativesRequest): String = dbQuery {
        Derivatives.insert {
            it[ticker] = derivativesRequest.ticker
            it[firstTrade] = derivativesRequest.firstTrade
            it[lastTrade] = derivativesRequest.lastTrade
            it[date] = derivativesRequest.date
            it[contractTypeId] = derivativesRequest.contractTypeId
        }[Derivatives.ticker]
    }

    suspend fun getAll(sortColumn: String?, sortOrder: String?, lang: String): List<DerivativesResponse> {
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
                val contractType = Translations.alias("contractType")

                Join(
                    Items, Derivatives,
                    onColumn = Items.ticker, otherColumn = Derivatives.ticker,
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
                ).join(
                    contractType,
                    onColumn = Derivatives.contractTypeId,
                    otherColumn = contractType[Translations.contentId],
                    joinType = JoinType.INNER,
                    additionalConstraint = { contractType[Translations.language] eq lang }
                )
                    .selectAll()
                    .orderBy(column, orderBy)
                    .map {
                        DerivativesResponse(
                            ticker = it[Items.ticker],
                            name = it[name[Translations.text]],
                            shortName = it[shortName[Translations.text]],
                            description = it[description[Translations.text]],
                            type = it[Items.type],
                            market = it[Items.market],
                            firstTrade = it[Derivatives.firstTrade],
                            lastTrade = it[Derivatives.lastTrade],
                            date = it[Derivatives.date],
                            contractType = it[contractType[Translations.text]]
                        )
                    }

            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun read(ticker: String, lang: String): DerivativesResponse? {
        return dbQuery {
            val name = Translations.alias("name")
            val shortName = Translations.alias("shortName")
            val description = Translations.alias("description")
            val contractType = Translations.alias("contractType")

            Join(
                Items, Derivatives,
                onColumn = Items.ticker,
                otherColumn = Derivatives.ticker,
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
            ).join(
                contractType,
                onColumn = Derivatives.contractTypeId,
                otherColumn = contractType[Translations.contentId],
                joinType = JoinType.INNER,
                additionalConstraint = { contractType[Translations.language] eq lang }
            ).select { Items.ticker eq ticker }.map {
                DerivativesResponse(
                    ticker = it[Items.ticker],
                    name = it[name[Translations.text]],
                    shortName = it[shortName[Translations.text]],
                    description = it[description[Translations.text]],
                    type = it[Items.type],
                    market = it[Items.market],
                    firstTrade = it[Derivatives.firstTrade],
                    lastTrade = it[Derivatives.lastTrade],
                    date = it[Derivatives.date],
                    contractType = it[contractType[Translations.text]]
                )
            }
        }.singleOrNull()
    }
}
