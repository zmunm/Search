package io.github.zmunm.search.data.service.dao

internal data class ResponseList<T>(
    val documents: List<T>,
    val meta: ResponseMeta,
)
