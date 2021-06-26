package io.github.zmunm.search.entity

import java.util.Date

data class Document(
    val documentType: DocumentType,
    val name: String,
    val contents: String,
    val datetime: Date?,
    val thumbnail: String,
    val title: String,
    val url: String,
)
