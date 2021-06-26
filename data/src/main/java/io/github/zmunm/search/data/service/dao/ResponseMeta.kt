package io.github.zmunm.search.data.service.dao

internal data class ResponseMeta(
    val is_end: Boolean,
    val pageable_count: Int,
    val total_count: Int,
)
