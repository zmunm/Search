package io.github.zmunm.search.repository

import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.entity.DocumentList
import io.github.zmunm.search.entity.Recent
import io.github.zmunm.search.entity.Visit
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

    fun getVisit(url: String): Flow<Visit>

    suspend fun putVisit(url: String)

    fun getRecent(): Flow<List<Recent>>

    suspend fun putRecent(query: String)
}
