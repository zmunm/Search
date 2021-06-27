package io.github.zmunm.search.data.cache.table

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "recent")
internal data class TableRecent(
    @PrimaryKey
    val query: String,
    val datetime: Date,
)
