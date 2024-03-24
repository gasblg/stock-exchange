package com.github.gasblg.db

import com.github.gasblg.db.entity.Items
import com.github.gasblg.db.entity.Translations
import com.github.gasblg.ext.dbQuery
import com.github.gasblg.models.items.ItemResponse
import com.github.gasblg.models.paging.PageParams
import com.github.gasblg.models.paging.PagedData
import com.github.gasblg.models.paging.getData
import com.github.gasblg.models.paging.getEmptyList
import org.jetbrains.exposed.sql.*


class SearchSchema {

    suspend fun getAll(pageParams: PageParams, q: String, lang: String): PagedData<ItemResponse> {
        return try {
            dbQuery {
                val name = Translations.alias("name")
                val shortName = Translations.alias("shortName")
                val description = Translations.alias("description")

                val query = Join(
                    Items, name,
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
                ).select {
                    Items.ticker.regexp(stringParam(q), false)
                }
                val total = query.count()

                val list = query
                    .limit(pageParams.count, offset = pageParams.offset)
                    .map {
                        ItemResponse(
                            ticker = it[Items.ticker],
                            name = it[name[Translations.text]],
                            shortName = it[shortName[Translations.text]],
                            description = it[description[Translations.text]],
                            type = it[Items.type],
                            market = it[Items.market]
                        )
                    }
                pageParams.getData(list, total)
            }
        } catch (e: Exception) {
            pageParams.getEmptyList()
        }
    }

}