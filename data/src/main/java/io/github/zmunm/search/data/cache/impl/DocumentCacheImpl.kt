package io.github.zmunm.search.data.cache.impl

import io.github.zmunm.search.data.cache.DocumentCache
import io.github.zmunm.search.data.cache.dao.DocumentDao
import io.github.zmunm.search.data.cache.table.TableVisit
import io.github.zmunm.search.data.cache.toEntity
import io.github.zmunm.search.data.cache.toTable
import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.entity.Visit
import java.util.Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

internal class DocumentCacheImpl(
    private val documentDao: DocumentDao,
) : DocumentCache {

    override fun getDocument(url: String): Flow<Document> = documentDao.getDocument(url)
        .filterNotNull().map {
            it.toEntity()
        }

    override suspend fun insertDocument(document: Document) {
        documentDao.upsert(document.toTable())
    }

    override fun getVisit(url: String): Flow<Visit> = documentDao.getVisit(url)
        .filterNotNull().map {
            it.toEntity()
        }

    override suspend fun insertVisit(url: String) {
        documentDao.insertVisit(TableVisit(url, Date()))
    }
}
