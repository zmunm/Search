package io.github.zmunm.search.data.cache.table

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "visit",
    foreignKeys = [
        ForeignKey(
            entity = TableDocument::class,
            parentColumns = ["url"],
            childColumns = ["url"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
internal data class TableVisit(
    @PrimaryKey
    val url: String,
    val datetime: Date,
)
