package io.github.zmunm.search.data.service.dao

import java.util.Date

internal data class ResponseCafe(
    val cafename: String,
    val contents: String,
    val datetime: Date?,
    val thumbnail: String,
    val title: String,
    val url: String
)
