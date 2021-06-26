package io.github.zmunm.search.data.cache

import io.github.zmunm.search.entity.Document
import kotlinx.coroutines.flow.Flow

interface DocumentCache {
    fun getDocument(url: String): Flow<Document>

    suspend fun insertDocument(document: Document)
}
