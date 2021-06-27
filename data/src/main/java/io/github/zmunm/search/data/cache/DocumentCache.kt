package io.github.zmunm.search.data.cache

import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.entity.Recent
import io.github.zmunm.search.entity.Visit
import kotlinx.coroutines.flow.Flow

interface DocumentCache {
    fun getDocument(url: String): Flow<Document>

    suspend fun insertDocument(document: Document)

    fun getVisit(url: String): Flow<Visit>

    suspend fun insertVisit(url: String)

    fun getRecent(): Flow<List<Recent>>

    suspend fun insertRecent(query: String)
}
