package com.github.gasblg.db

import com.github.gasblg.db.entity.Candles
import com.github.gasblg.db.entity.Items
import com.github.gasblg.db.entity.Translations
import com.github.gasblg.ext.dbQuery
import com.github.gasblg.models.candles.CandleResponse
import com.github.gasblg.models.paging.PageParams
import com.github.gasblg.models.paging.PagedData
import com.github.gasblg.models.paging.getData
import com.github.gasblg.models.paging.getEmptyList
import com.github.gasblg.models.shares.SharesItemResponse
import com.github.gasblg.models.shares.SharesDetailResponse
import org.jetbrains.exposed.sql.*

class SharesSchema {

    suspend fun getAll(
        pageParams: PageParams,
        sortColumn: String?,
        sortOrder: String?,
        lang: String
    ): PagedData<SharesItemResponse> {

        val orderBy = when (sortOrder) {
            "desc" -> SortOrder.DESC
            else -> SortOrder.ASC
        }

        return try {
            dbQuery {

                val i = Items.alias("i")
                val c1 = Candles.alias("c1")
                val c2 = Candles.alias("c2")
                val name = Translations.alias("name")
                val shortName = Translations.alias("shortName")
                val description = Translations.alias("description")

                val column = when (sortColumn) {
                    "ticker" -> i[Items.ticker]
                    "shortname" -> i[Items.shortNameId]
                    "percent" -> c1[Candles.percent]
                    else -> i[Items.ticker]
                }

                val query = Join(
                    i, c1,
                    onColumn = i[Items.ticker],
                    otherColumn = c1[Candles.ticker],
                    joinType = JoinType.INNER
                ).join(
                    c2,
                    onColumn = i[Items.ticker],
                    otherColumn = c2[Candles.ticker],
                    joinType = JoinType.LEFT,
                    additionalConstraint = { c1[Candles.date] less c2[Candles.date] }
                ).join(
                    name,
                    onColumn = i[Items.nameId],
                    otherColumn = name[Translations.contentId],
                    joinType = JoinType.INNER,
                    additionalConstraint = { name[Translations.language] eq lang }
                ).join(
                    shortName,
                    onColumn = i[Items.shortNameId],
                    otherColumn = shortName[Translations.contentId],
                    joinType = JoinType.INNER,
                    additionalConstraint = { shortName[Translations.language] eq lang }
                ).join(
                    description,
                    onColumn = i[Items.descriptionId],
                    otherColumn = description[Translations.contentId],
                    joinType = JoinType.INNER,
                    additionalConstraint = { description[Translations.language] eq lang }
                ).select { c2[Candles.ticker].isNull() }

                val total = query.count()

                val list = query
                    .orderBy(column, orderBy)
                    .limit(pageParams.count, offset = pageParams.offset)
                    .map {
                        SharesItemResponse(
                            ticker = it[i[Items.ticker]],
                            date = it[c1[Candles.date]],
                            name = it[name[Translations.text]],
                            shortName = it[shortName[Translations.text]],
                            description = it[description[Translations.text]],
                            type = it[i[Items.type]],
                            market = it[i[Items.market]],
                            price = it[c1[Candles.price]],
                            open = it[c1[Candles.open]],
                            low = it[c1[Candles.low]],
                            high = it[c1[Candles.high]],
                            close = it[c1[Candles.close]],
                            volume = it[c1[Candles.volume]],
                            percent = it[c1[Candles.percent]],
                        )
                    }
                pageParams.getData(list, total)
            }
        } catch (e: Exception) {
            pageParams.getEmptyList()
        }
    }

    suspend fun read(ticker: String, last: Int, lang: String): SharesDetailResponse? {
        return dbQuery {

            val name = Translations.alias("name")
            val shortName = Translations.alias("shortName")
            val description = Translations.alias("description")

            val candles = Candles.select { Candles.ticker eq ticker }.limit(last).map {
                CandleResponse(
                    ticker = it[Candles.ticker],
                    date = it[Candles.date],
                    price = it[Candles.price],
                    open = it[Candles.open],
                    low = it[Candles.low],
                    high = it[Candles.high],
                    close = it[Candles.close],
                    volume = it[Candles.volume],
                    percent = it[Candles.percent]
                )
            }

            val lastDate = candles.lastOrNull()?.date ?: ""

            Join(
                Items,
                Candles,
                onColumn = Items.ticker,
                otherColumn = Candles.ticker,
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
            ).select { (Items.ticker eq ticker) and (Candles.date eq lastDate) }.map {
                SharesDetailResponse(
                    ticker = it[Items.ticker],
                    date = it[Candles.date],
                    name = it[name[Translations.text]],
                    shortName = it[shortName[Translations.text]],
                    description = it[description[Translations.text]],
                    type = it[Items.type],
                    market = it[Items.market],
                    open = it[Candles.open],
                    low = it[Candles.low],
                    high = it[Candles.high],
                    close = it[Candles.close],
                    volume = it[Candles.volume],
                    price = it[Candles.price],
                    percent = it[Candles.percent],
                    candles = candles
                )
            }
        }.singleOrNull()
    }

}