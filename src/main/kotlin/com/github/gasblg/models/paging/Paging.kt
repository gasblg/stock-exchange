package com.github.gasblg.models.paging


object Paging {

    const val DEFAULT_PAGE_SIZE = 10
    const val FIRST_PAGE = 1
}

open class PageParams(val page: Int = Paging.FIRST_PAGE, val count: Int = Paging.DEFAULT_PAGE_SIZE) {

    val offset: Long
        get() = ((page - 1) * count).toLong()
}

class PagedData<T>(val items: List<T>, val total: Long, page: Int, count: Int) : PageParams(page, count) {

    val previous: Int? by lazy {
        val totalPage = getTotalPage(total.toInt(), count)
        val hasPreviousPage = page in (Paging.FIRST_PAGE + 1)..totalPage
        if (hasPreviousPage) page - 1 else null
    }

    val next: Int? by lazy {
        if (count == items.size) page + 1 else null
    }

    private fun getTotalPage(total: Int, count: Int): Int {
        val extra = if (total % count == 0) 0 else 1
        return (total / count) + extra
    }
}

fun <T> PageParams.getData(items: List<T>, total: Long): PagedData<T> {
    return PagedData(items, total, page, count)
}

fun <T> PageParams.getEmptyList(): PagedData<T> {
    return PagedData(listOf(), 0, page, count)
}

