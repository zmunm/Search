package io.github.zmunm.search.data.cache.dao

import androidx.room.Dao
import androidx.room.Query
import io.github.zmunm.search.data.cache.table.TableDocument
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class DocumentDao : BaseDao<TableDocument>() {
    @Query("SELECT * FROM document WHERE url = :url")
    abstract fun getDocument(url: String): Flow<TableDocument>
}
