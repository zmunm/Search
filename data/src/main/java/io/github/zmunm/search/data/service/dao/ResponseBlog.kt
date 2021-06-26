package io.github.zmunm.search.data.service.dao

import java.util.Date

internal data class ResponseBlog(
    val blogname: String,
    val contents: String,
    val datetime: Date?,
    val thumbnail: String,
    val title: String,
    val url: String
)
