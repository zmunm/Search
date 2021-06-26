package io.github.zmunm.search.data.cache.table

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.zmunm.search.entity.DocumentType
import java.util.Date

@Entity(tableName = "document")
internal data class TableDocument(
    @PrimaryKey
    val url: String,
    val documentType: DocumentType,
    val name: String,
    val contents: String,
    val datetime: Date?,
    val thumbnail: String,
    val title: String,
)
