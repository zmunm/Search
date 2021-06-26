package io.github.zmunm.search.repository

import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.entity.DocumentList
import kotlinx.coroutines.flow.Flow

interface DocumentRepository {
    suspend fun getBlogs(
        query: String,
        sort: String?,
        page: Int?,
        size: Int?,
    ): DocumentList

    suspend fun getCafes(
        query: String,
        sort: String?,
        page: Int?,
        size: Int?,
    ): DocumentList

    fun getDocument(url: String): Flow<Document>

    suspend fun putDocument(document: Document)
}
