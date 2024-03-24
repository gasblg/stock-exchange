package com.github.gasblg.models

import com.github.gasblg.enums.Language
import com.github.gasblg.models.paging.Paging
import io.ktor.http.*

object Params {

    private const val PAGE_PARAM = "page"
    private const val PAGE_SIZE = "page_size"
    private const val LANG = "lang"
    private const val ID = "id"
    private const val TICKER = "ticker"
    private const val LAST = "last"
    private const val SORT_COLUMN = "sort_column"
    private const val SORT_ORDER = "sort_order"
    private const val QUERY = "q"

    fun Parameters.getPage(): Int {
        return this[PAGE_PARAM]?.toIntOrNull()?.let { page ->
            if (page < Paging.FIRST_PAGE) Paging.FIRST_PAGE else page
        } ?: Paging.FIRST_PAGE
    }

    fun Parameters.getPageSize(): Int {
        return this[PAGE_SIZE]?.toIntOrNull()?.let { size ->
            if (size <= 0) Paging.DEFAULT_PAGE_SIZE else size
        } ?: Paging.DEFAULT_PAGE_SIZE
    }

    fun Parameters.getLang(): String {
        return this[LANG] ?: Language.EN.type
    }

    fun Parameters.getId(): Int {
        return this[ID]?.toIntOrNull() ?: throw IllegalArgumentException("Invalid ID")
    }

    fun Parameters.getLangId(): String {
        return this[ID] ?: throw IllegalArgumentException("Invalid ID")
    }

    fun Parameters.getTicker(): String {
        return this[TICKER] ?: throw IllegalArgumentException("Invalid ticker")
    }

    fun Parameters.getLast(): Int {
        return this[LAST]?.toIntOrNull() ?: 60
    }

    fun Parameters.getSortColumn(): String {
        return this[SORT_COLUMN] ?: ""
    }

    fun Parameters.getSortOrder(): String {
        return this[SORT_ORDER] ?: ""
    }

    fun Parameters.getQuery(): String {
        return this[QUERY] ?: ""
    }
}

