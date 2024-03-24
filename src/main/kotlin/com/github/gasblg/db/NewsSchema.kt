package com.github.gasblg.db

import com.github.gasblg.db.entity.News
import com.github.gasblg.db.entity.Translations
import com.github.gasblg.ext.dbQuery
import com.github.gasblg.models.news.NewsDetailResponse
import com.github.gasblg.models.news.NewsResponse
import com.github.gasblg.models.news.NewsRequest
import com.github.gasblg.models.paging.PageParams
import com.github.gasblg.models.paging.PagedData
import com.github.gasblg.models.paging.getData
import com.github.gasblg.models.paging.getEmptyList
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction


class NewsSchema {

    suspend fun create(newsRequest: NewsRequest) = dbQuery {
        News.insert {
            it[titleId] = newsRequest.titleId
            it[date] = newsRequest.date
            it[bodyId] = newsRequest.bodyId
        }[News.id]
    }

    suspend fun getAll(pageParams: PageParams, lang: String): PagedData<NewsResponse> {
        return try {
            newSuspendedTransaction(Dispatchers.IO) {

                val total = News.selectAll().count()

                val n = News.alias("n")
                val title = Translations.alias("title")

                val list = Join(
                    n, title,
                    onColumn = n[News.titleId],
                    otherColumn = title[Translations.contentId],
                    joinType = JoinType.INNER,
                ).select { title[Translations.language] eq lang }
                    .limit(pageParams.count, offset = pageParams.offset)
                    .map {
                        NewsResponse(
                            id = it[n[News.id]],
                            title = it[title[Translations.text]],
                            date = it[n[News.date]]
                        )
                    }
                pageParams.getData(list, total)
            }

        } catch (e: Exception) {
            pageParams.getEmptyList()
        }
    }


    suspend fun read(id: Int, lang: String): NewsDetailResponse? {
        return dbQuery {
            val n = News.alias("n")
            val title = Translations.alias("title")
            val body = Translations.alias("body")

            Join(
                n, title,
                onColumn = n[News.titleId],
                otherColumn = title[Translations.contentId],
                joinType = JoinType.INNER,
                additionalConstraint = {
                    title[Translations.language] eq lang
                }
            ).join(
                body,
                onColumn = n[News.bodyId],
                otherColumn = body[Translations.contentId],
                joinType = JoinType.INNER,
                additionalConstraint = {
                    body[Translations.language] eq lang
                }
            ).select { (n[News.id] eq id) }
                .map {
                    NewsDetailResponse(
                        id = it[n[News.id]],
                        title = it[title[Translations.text]],
                        date = it[n[News.date]],
                        body = it[body[Translations.text]]
                    )
                }.firstOrNull()
        }
    }
}