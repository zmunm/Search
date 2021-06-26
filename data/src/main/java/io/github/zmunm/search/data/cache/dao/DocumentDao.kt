package io.github.zmunm.search.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.zmunm.search.data.cache.table.TableDocument
import io.github.zmunm.search.data.cache.table.TableVisit
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class DocumentDao : BaseDao<TableDocument>() {
    @Query("SELECT * FROM document WHERE url = :url")
    abstract fun getDocument(url: String): Flow<TableDocument>

    @Query("SELECT * FROM visit WHERE url = :url")
    abstract fun getVisit(url: String): Flow<TableVisit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertVisit(visit: TableVisit)
}
