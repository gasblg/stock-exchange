package com.github.gasblg.db

import com.github.gasblg.db.entity.Candles
import com.github.gasblg.ext.dbQuery
import com.github.gasblg.models.candles.CandleRequest
import com.github.gasblg.models.candles.CandleResponse
import com.github.gasblg.models.paging.PageParams
import com.github.gasblg.models.paging.PagedData
import com.github.gasblg.models.paging.getData
import com.github.gasblg.models.paging.getEmptyList
import org.jetbrains.exposed.sql.*

class CandlesSchema {

    suspend fun create(candle: CandleRequest): CandleResponse? = dbQuery {
        val c = Candles.insert {
            it[ticker] = candle.ticker
            it[date] = candle.date
            it[price] = candle.price
            it[open] = candle.open
            it[low] = candle.low
            it[high] = candle.high
            it[close] = candle.close
            it[volume] = candle.volume
            it[percent] = candle.percent
        }
        CandleResponse(
            ticker = c[Candles.ticker],
            date = c[Candles.date],
            open = c[Candles.open],
            low = c[Candles.low],
            high = c[Candles.high],
            close = c[Candles.close],
            volume = c[Candles.volume],
            price = c[Candles.price],
            percent = c[Candles.percent]
        )
    }


    suspend fun getAll(pageParams: PageParams): PagedData<CandleResponse> {
        return try {
            dbQuery {
                val total = Candles.selectAll().count()
                val list = Candles.selectAll()
                    .limit(pageParams.count, offset = pageParams.offset)
                    .map {
                        CandleResponse(
                            ticker = it[Candles.ticker],
                            date = it[Candles.date],
                            open = it[Candles.open],
                            low = it[Candles.low],
                            high = it[Candles.high],
                            close = it[Candles.close],
                            volume = it[Candles.volume],
                            price = it[Candles.price],
                            percent = it[Candles.percent]
                        )
                    }
                pageParams.getData(list, total)
            }
        } catch (e: Exception) {
            pageParams.getEmptyList()
        }
    }
}